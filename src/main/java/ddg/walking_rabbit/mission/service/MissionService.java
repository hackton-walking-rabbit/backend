package ddg.walking_rabbit.mission.service;

import ddg.walking_rabbit.global.domain.entity.MissionEntity;
import ddg.walking_rabbit.global.domain.entity.MissionStatus;
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

    public MissionDto createMission(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저가 존재하지 않습니다."));

        boolean isNormal = Math.random() < 0.8;



        String content = webClient.post()
                .uri("/api/mission")
                .retrieve()
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        resp -> resp.createException().flatMap(Mono::error)
                )
                .bodyToMono(String.class)
                .block();

        // 랜덤한 공원 랜덤 돌리기
        // 전체 공원 디비에 저장 -> 랜덤돌려서 ~에서 만들기
        if (!isNormal) {
            String park = ""+ "에서 ";
            content = park + content;
        }

        MissionEntity mission = new MissionEntity();
        mission.setUser(user);
        mission.setContent(content);
        mission.setMissionDate(LocalDate.now());
        mission.setMissionType(isNormal ? MissionType.GENERAL : MissionType.LOCAL);
        mission.setIsSuccess(MissionStatus.NONE);
        missionRepository.save(mission);

        MissionDto result = new MissionDto();
        result.setMissionId(mission.getMissionId());
        result.setContent(mission.getContent());
        return result;
    }

    public MissionResponseDto findMission(Long userId){
        LocalDate date = LocalDate.now();
        MissionEntity mission = missionRepository.findTopByUser_UserIdAndMissionDateOrderByMissionIdDesc(userId, date);

        if (mission == null) {
            throw new IllegalArgumentException("오늘의 미션을 생성해주세요!");
        }

        MissionResponseDto result = new MissionResponseDto();
        result.setMissionId(mission.getMissionId());
        result.setContent(mission.getContent());
        result.setIsSuccess(mission.getIsSuccess().toString());
        return result;
    }
}
