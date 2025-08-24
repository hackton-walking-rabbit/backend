package ddg.walking_rabbit.global.domain.repository;

import ddg.walking_rabbit.global.domain.entity.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
    MissionEntity findByMissionId(Long missionId);

    MissionEntity findByUser_UserIdAndMissionDate(Long userId, LocalDate date);
}
