package ddg.walking_rabbit.global.domain.repository;

import ddg.walking_rabbit.global.domain.entity.ContentType;
import ddg.walking_rabbit.global.domain.entity.ConversationEntity;
import ddg.walking_rabbit.global.domain.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    @Query("select m.content from MessageEntity m where m.conversation = :conversation order by m.messageId asc")
    List<String> findAllContentByConversationOrderByMessageIdAsc(ConversationEntity conversation);

    Optional<MessageEntity> findByConversation_ConversationIdAndContentType(Long conversationId, ContentType contentType);

    List<MessageEntity> findAllByConversationOrderByMessageIdAsc(ConversationEntity conversation);
}
