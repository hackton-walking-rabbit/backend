package ddg.walking_rabbit.global.domain.repository;

import ddg.walking_rabbit.global.domain.entity.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
    MissionEntity findByMissionId(Long missionId);
}
