package ddg.walking_rabbit.message.repository;

import ddg.walking_rabbit.message.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {
}
