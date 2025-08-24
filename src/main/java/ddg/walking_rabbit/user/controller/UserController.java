package ddg.walking_rabbit.user.controller;

import ddg.walking_rabbit.global.domain.entity.UserEntity;
import ddg.walking_rabbit.global.response.SuccessResponse;
import ddg.walking_rabbit.user.dto.NicknameUpdateDto;
import ddg.walking_rabbit.user.dto.SignUpRequestDto;
import ddg.walking_rabbit.user.dto.TokenResponseDto;
import ddg.walking_rabbit.user.dto.UserInfoDto;
import ddg.walking_rabbit.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @GetMapping("/info")
    public ResponseEntity<SuccessResponse<UserInfoDto>> getUserInfo() {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserInfoDto result = userService.getUserInfo(user);
        return SuccessResponse.onSuccess("유저의 정보를 조회하였습니다.", HttpStatus.OK, result);
    }

    @PatchMapping("/nickname")
    public ResponseEntity<SuccessResponse<Void>> updateNickname(@RequestBody NicknameUpdateDto nicknameDto){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updateNickname(user, nicknameDto.getNickname());
        return SuccessResponse.ok("유저의 닉네임을 성공적으로 수정했습니다.");
    }

    @PatchMapping("/profile-image")
    public ResponseEntity<SuccessResponse<Void>> updateProfileImage(@RequestPart("file") MultipartFile file) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.updateProfileImage(user, file);
        return SuccessResponse.ok("유저의 프로필 이미지를 성공적으로 수정했습니다.");
    }

}
