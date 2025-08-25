package ddg.walking_rabbit.global.domain.repository;

import ddg.walking_rabbit.global.domain.entity.ChatRecordEntity;
import ddg.walking_rabbit.global.domain.entity.ConversationEntity;
import ddg.walking_rabbit.global.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatRecordRepository extends JpaRepository<ChatRecordEntity, Long> {

    List<ChatRecordEntity> findAllByTitleOrderByChatRecordId(String title);

    @Query("select c.user from ChatRecordEntity c where c.chatRecordId = :chatRecordId")
    UserEntity findUserByChatRecordId(Long chatRecordId);

    @Query("select c.conversation from ChatRecordEntity c where c.chatRecordId = :chatRecordId")
    ConversationEntity findConversationByChatRecordId(Long chatRecordId);

    ChatRecordEntity findByConversation(ConversationEntity conversation);

    @Query("select c from ChatRecordEntity c where c.user = :user and c.createdAt = :date order by c.chatRecordId asc")
    List<ChatRecordEntity> findAllByUserAndCreatedAtOrderByChatRecordIdAsc(UserEntity user, LocalDateTime date);

    @Query("select c from ChatRecordEntity c where c.user = :user and c.createdAt >= :start and c.createdAt < :end order by c.chatRecordId asc")
    List<ChatRecordEntity> findAllByUserAndBetweenDateOrderByChatRecordIdAsc(UserEntity user, LocalDateTime start, LocalDateTime end);
}
