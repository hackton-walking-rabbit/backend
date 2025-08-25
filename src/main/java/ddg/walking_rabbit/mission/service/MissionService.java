package ddg.walking_rabbit.mission.service;

import ddg.walking_rabbit.global.domain.entity.MissionEntity;
import ddg.walking_rabbit.global.domain.entity.MissionType;
import ddg.walking_rabbit.global.domain.entity.UserEntity;
import ddg.walking_rabbit.global.domain.repository.MissionRepository;
import ddg.walking_rabbit.global.domain.repository.UserRepository;
import ddg.walking_rabbit.mission.dto.MissionDto;
import ddg.walking_rabbit.mission.dto.MissionResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final UserRepository userRepository;
    private final WebClient webClient;

    public MissionResponseDto createMission(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        double random = Math.random();
        boolean isNormal = random < 0.8;

        String content = webClient.post()
                .uri("/api/mission")
                .bodyValue(isNormal ? MissionType.GENERAL : MissionType.LOCAL)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        resp -> resp.createException().flatMap(Mono::error)
                )
                .bodyToMono(String.class)
                .block();

        MissionEntity mission = new MissionEntity();
        mission.setUser(user);
        mission.setContent(content);
        mission.setMissionDate(LocalDate.now());
        missionRepository.save(mission);

        MissionResponseDto result = new MissionResponseDto();
        result.setMissionId(mission.getMissionId());
        result.setContent(mission.getContent());
        return result;
    }

    public MissionResponseDto findMission(Long userId){
        LocalDate date = LocalDate.now();
        MissionEntity mission = missionRepository.findByUser_UserIdAndMissionDate(userId, date);

        if (mission == null) {
            throw new IllegalArgumentException("오늘의 미션을 생성해주세요!");
        }

        MissionResponseDto result = new MissionResponseDto();
        result.setMissionId(mission.getMissionId());
        result.setContent(mission.getContent());
        return result;
    }
}
