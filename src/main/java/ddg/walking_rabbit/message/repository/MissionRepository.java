package ddg.walking_rabbit.message.repository;

import ddg.walking_rabbit.message.dto.ChatStartDto;
import ddg.walking_rabbit.message.entity.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
    MissionEntity findByMissionId(Long missionId);
}
