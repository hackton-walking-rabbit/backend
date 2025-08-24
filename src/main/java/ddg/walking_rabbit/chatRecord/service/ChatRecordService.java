package ddg.walking_rabbit.chatRecord.service;

import ddg.walking_rabbit.chatRecord.dto.MapResponseDto;
import ddg.walking_rabbit.chatRecord.dto.MessageDto;
import ddg.walking_rabbit.chatRecord.dto.MessageResponseDto;
import ddg.walking_rabbit.chatRecord.dto.SaveResponseDto;
import ddg.walking_rabbit.global.domain.entity.*;
import ddg.walking_rabbit.global.domain.repository.ChatRecordRepository;
import ddg.walking_rabbit.global.domain.repository.ConversationRepository;
import ddg.walking_rabbit.global.domain.repository.MessageRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRecordService {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final ChatRecordRepository chatRecordRepository;

    @Transactional
    public SaveResponseDto saveChatRecord(UserEntity user, Long conversationId) {
        ConversationEntity conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new EntityNotFoundException("대화가 존재하지 않습니다."));

        MessageEntity message = messageRepository.findByConversation_ConversationIdAndContentType(conversationId, ContentType.PHOTO)
                .orElseThrow(() -> new EntityNotFoundException("대화의 사진을 찾을 수 없습니다."));

        List<ChatRecordEntity> records = chatRecordRepository.findAllByTitleOrderByChatRecordId(conversation.getTitle());

        ChatRecordEntity chatRecord = new ChatRecordEntity();
        chatRecord.setUser(user);
        chatRecord.setConversation(conversation);
        chatRecord.setTitle(conversation.getTitle());
        chatRecord.setCoverPhoto(message.getContent());
        chatRecord.setExplorerOrder(records.size() + 1);
        chatRecord.setCreatedAt(LocalDateTime.now());
        chatRecordRepository.save(chatRecord);

        SaveResponseDto result = new SaveResponseDto();
        result.setChatRecordId(chatRecord.getChatRecordId());
        result.setExplorerOrder(chatRecord.getExplorerOrder());
        return result;
    }

    public MessageResponseDto getChatRecord(UserEntity user, Long chatRecordId) {
        if (user != chatRecordRepository.findUserByChatRecordId(chatRecordId)){
            throw new IllegalArgumentException("해당 채팅을 조회할 권한이 없습니다.");
        }

        ConversationEntity conversation = chatRecordRepository.findConversationByChatRecordId(chatRecordId);
        List<MessageEntity> messages = messageRepository.findAllByConversationOrderByMessageIdAsc(conversation);

        MessageResponseDto results = new MessageResponseDto();
        results.setMission(conversation.getMission().getContent());
        for (MessageEntity message : messages) {
            MessageDto result = new MessageDto();
            result.setMessageId(message.getMessageId());
            result.setRole(message.getRole());
            result.setContentType(message.getContentType());
            result.setContent(message.getContent());
            results.getMessages().add(result);
        }
        return results;
    }

    public List<MapResponseDto> getChatRecordMap(UserEntity user) {
        List<ConversationEntity> conversations = conversationRepository.findAllByUser(user);

        List<MapResponseDto> results = new ArrayList<>();
        for (ConversationEntity conversation : conversations) {
            MapResponseDto result = new MapResponseDto();
            ChatRecordEntity chatRecord = chatRecordRepository.findByConversation(conversation);
            result.setChatRecordId(chatRecord.getChatRecordId());
            result.setLatitude(conversation.getLatitude());
            result.setLongitude(conversation.getLongitude());
            result.setMissionId(conversation.getMission().getMissionId());
            results.add(result);
        }
        return results;
    }
}
