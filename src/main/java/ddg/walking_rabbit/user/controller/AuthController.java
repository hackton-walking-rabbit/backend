package ddg.walking_rabbit.user.controller;

import ddg.walking_rabbit.global.response.SuccessResponse;
import ddg.walking_rabbit.user.dto.KakaoLoginFailResponseDto;
import ddg.walking_rabbit.user.dto.KakaoResponseDto;
import ddg.walking_rabbit.user.dto.TokenResponseDto;
import ddg.walking_rabbit.user.entity.UserEntity;
import ddg.walking_rabbit.user.service.AuthService;
import ddg.walking_rabbit.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Value("${kakao.auth.client}")
    private String clientId;

    @Value("${kakao.auth.redirect}")
    private String redirectUri;

    @GetMapping("/request")
    public ResponseEntity<SuccessResponse<String>> getKakaoUri(){

        String url = "https://kauth.kakao.com/oauth/authorize?"
                + "response_type=code&"
                + "client_id=" + clientId
                + "&redirect_uri=" + redirectUri;

        return SuccessResponse.onSuccess("이동해야할 url", HttpStatus.OK, url);
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<SuccessResponse<Object>> kakaoLogin(@RequestParam("code") String code, HttpServletResponse response) {

        KakaoResponseDto.OAuthToken token = authService.getAccessToken(code);
        KakaoResponseDto.KakaoProfile profile = authService.getUserInfo(token.getAccessToken());
        Long kakaoId = profile.getId();
        String nickname = profile.getKaKaoAccount().getProfile().getNickname();
        String profileImageUrl = profile.getKaKaoAccount().getProfile().getProfileImageUrl();

        Optional<UserEntity> optionalUser = userService.findUserByKakaoId(kakaoId);

        if (optionalUser.isEmpty()) {
            KakaoLoginFailResponseDto loginFailResponseDto = new KakaoLoginFailResponseDto();
            loginFailResponseDto.setKakaoId(kakaoId);
            loginFailResponseDto.setNickname(nickname);
            loginFailResponseDto.setProfileImageUrl(profileImageUrl);
            return SuccessResponse.onSuccess("회원가입 필요", HttpStatus.OK, loginFailResponseDto);
        }

        UserEntity user = optionalUser.get();
        String jwt = authService.login(user);
        TokenResponseDto tokenResponseDto = new TokenResponseDto(jwt);
        return SuccessResponse.onSuccess("로그인 성공", HttpStatus.OK, tokenResponseDto);
    }
}
