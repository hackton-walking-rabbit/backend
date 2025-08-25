package ddg.walking_rabbit.message.service;

import com.google.cloud.storage.*;
import ddg.walking_rabbit.global.domain.entity.*;
import ddg.walking_rabbit.message.config.WebClientConfig;
import ddg.walking_rabbit.message.dto.*;
import ddg.walking_rabbit.global.domain.repository.ConversationRepository;
import ddg.walking_rabbit.global.domain.repository.MessageRepository;
import ddg.walking_rabbit.global.domain.repository.MissionRepository;
import ddg.walking_rabbit.global.domain.entity.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MissionRepository missionRepository;
    private final ConversationRepository conversationRepository;
    private final Storage storage;
    private final WebClient webClient;

    @Value("${gcp.storage.bucket.name}")
    private String bucketName;

    @Transactional
    public ChatResponseDto startChat(UserEntity user, MultipartFile file, ChatStartDto chatStartDto){
        ConversationEntity conversation = new ConversationEntity();
        conversation.setUser(user);
        conversation.setLatitude(chatStartDto.getLatitude());
        conversation.setLongitude(chatStartDto.getLongitude());

        MissionEntity mission = missionRepository.findByMissionId(chatStartDto.getMissionId());
        conversation.setMission(mission);
        conversationRepository.save(conversation);

        MessageEntity userMessage = uploadImage(file, conversation);

        // 모델 요청 바디
        ModelRequestDto requestDto = new ModelRequestDto();
        requestDto.setPhoto(userMessage.getContent());
        requestDto.setMission(mission != null ? mission.getContent() : null);

        ModelResponseDto responseDto = webClient.post()
                .uri("/api/photo")
                .bodyValue(requestDto)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        resp -> resp.createException().flatMap(Mono::error)
                )
                .bodyToMono(ModelResponseDto.class)
                .block();

        if (responseDto == null) {
            throw new IllegalArgumentException("응답이 옳지 않습니다");
        }

         // 미션인경우
        if (responseDto.getIsSuccess() != null) {
            if (!responseDto.getIsSuccess()) {
                throw new IllegalArgumentException("미션을 실패하셨습니다.");
            }
            if (conversation.getMission().getMissionType() == MissionType.LOCAL) {
                String[] parts = conversation.getMission().getContent().split("에서");
                // 공원 및 거리 저장된 엔티티 SpotEntityRepository라고 합세
                // 근데 그 안의 범위가 아니면 머시기저시기 postGresSQL 폴리곤 써야할듯 난중에 하자
                boolean success = false;
                if (!success) {
                    throw new IllegalArgumentException("미션을 실패하셨습니다");
                }
            }
        }

        MessageEntity aiMessage = new MessageEntity();
        aiMessage.setContent(responseDto.getAnswer());
        aiMessage.setConversation(conversation);
        aiMessage.setRole(Role.ASSISTANT);
        aiMessage.setContentType(ContentType.TEXT);
        messageRepository.save(aiMessage);

        String title = responseDto.getTitle();
        conversation.setTitle(title);
        conversationRepository.save(conversation);

        ChatResponseDto result = new ChatResponseDto();
        result.setMessageId(aiMessage.getMessageId());
        result.setConversationId(conversation.getConversationId());
        result.setRole(Role.ASSISTANT);
        result.setContentType(ContentType.TEXT);
        result.setContent(aiMessage.getContent());
        result.setKeyword(responseDto.getKeyword());
        return result;
    }

    @Transactional
    public MessageEntity uploadImage(MultipartFile file, ConversationEntity conversation) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일입니다.");
        }

        String newFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, newFilename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        try {
            Blob blob = storage.create(blobInfo, file.getBytes());
            if (blob == null || blob.getSize() == 0) {
                throw new IllegalArgumentException("GCS 업로드 중 오류가 발생했습니다.");
            }
        } catch(IOException | StorageException e){
            throw new IllegalArgumentException("GCS 업로드 중 오류가 발생했습니다.");
        }

        String publicUrl = String.format("https://storage.googleapis.com/%s/%s",
                bucketName, URLEncoder.encode(newFilename, StandardCharsets.UTF_8));

        MessageEntity message = new MessageEntity();
        message.setRole(Role.USER);
        message.setContentType(ContentType.PHOTO);
        message.setContent(publicUrl);
        message.setConversation(conversation);
        messageRepository.save(message);
        return message;
    }

    @Transactional
    public ChatResponseDto doChat(UserEntity user, Long conversationId, String content) {
        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new EntityNotFoundException("conversation이 존재하지 않습니다."));

        // 유저 대화 저장
        MessageEntity userMessage = new MessageEntity();
        userMessage.setRole(Role.USER);
        userMessage.setContentType(ContentType.TEXT);
        userMessage.setContent(content);
        userMessage.setConversation(conversation);
        messageRepository.save(userMessage);

        // 챗봇 통신
        List<String> messages = messageRepository.findAllContentByConversationOrderByMessageIdAsc(conversation);

        ModelShortResponseDto responseDto = webClient.post()
                .uri("/api/message")
                .bodyValue(messages)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        resp -> resp.createException().flatMap(Mono::error)
                )
                .bodyToMono(ModelShortResponseDto.class)
                .block();

        if (responseDto == null) {
            throw new IllegalArgumentException("응답이 옳지 않습니다");
        }

        MessageEntity aiMessage = new MessageEntity();
        aiMessage.setRole(Role.ASSISTANT);
        aiMessage.setContentType(ContentType.TEXT);
        aiMessage.setContent(responseDto.getAnswer());
        aiMessage.setConversation(conversation);
        messageRepository.save(aiMessage);

        ChatResponseDto result = ChatResponseDto.builder()
                .messageId(aiMessage.getMessageId())
                .role(Role.ASSISTANT)
                .contentType(ContentType.TEXT)
                .content(aiMessage.getContent())
                .conversationId(conversationId)
                .keyword(responseDto.getKeyword())
                .build();

        return result;
    }
}
