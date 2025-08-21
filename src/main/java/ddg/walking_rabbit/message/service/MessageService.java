package ddg.walking_rabbit.message.service;

import com.google.cloud.storage.*;
import ddg.walking_rabbit.global.domain.entity.*;
import ddg.walking_rabbit.message.dto.ChatResponseDto;
import ddg.walking_rabbit.message.dto.ChatStartDto;
import ddg.walking_rabbit.global.domain.repository.ConversationRepository;
import ddg.walking_rabbit.global.domain.repository.MessageRepository;
import ddg.walking_rabbit.global.domain.repository.MissionRepository;
import ddg.walking_rabbit.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MissionRepository missionRepository;
    private final ConversationRepository conversationRepository;
    private final Storage storage;

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

        // 챗봇 연결
        RestTemplate restTemplate = new RestTemplate();

        String answer ="abc";

        MessageEntity aiMessage = new MessageEntity();
        aiMessage.setRole(Role.ASSISTANT);
        aiMessage.setContentType(ContentType.TEXT);
        aiMessage.setContent(answer);
        aiMessage.setConversation(conversation);
        messageRepository.save(aiMessage);

        ChatResponseDto result = new ChatResponseDto();
        result.setMessageId(aiMessage.getMessageId());
        result.setConversationId(conversation.getConversationId());
        result.setRole(Role.ASSISTANT);
        result.setContentType(ContentType.TEXT);
        result.setContent(aiMessage.getContent());
        return result;
    }

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

    public ChatResponseDto doChat(Long userId, Long conversationId, String content) {
        return null;
    }
}
