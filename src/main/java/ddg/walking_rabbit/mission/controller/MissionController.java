package ddg.walking_rabbit.mission.controller;

import ddg.walking_rabbit.global.domain.entity.UserEntity;
import ddg.walking_rabbit.global.response.SuccessResponse;
import ddg.walking_rabbit.mission.dto.MissionDto;
import ddg.walking_rabbit.mission.dto.MissionResponseDto;
import ddg.walking_rabbit.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @PostMapping("/create")
    public ResponseEntity<SuccessResponse<MissionResponseDto>> createMission(){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUserId();
        MissionResponseDto responseDto = missionService.createMission(userId);
        return SuccessResponse.onSuccess("오늘의 미션을 성공적으로 생성했습니다.", HttpStatus.CREATED, responseDto);
    }

    @GetMapping("/my")
    public ResponseEntity<SuccessResponse<MissionResponseDto>> getMission(){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = user.getUserId();
        MissionResponseDto responseDto = missionService.findMission(userId);
        return SuccessResponse.onSuccess("오늘의 미션을 성공적으로 조회했습니다.", HttpStatus.OK, responseDto);
    }
}
