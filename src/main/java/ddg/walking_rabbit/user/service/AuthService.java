package ddg.walking_rabbit.user.service;

import ddg.walking_rabbit.global.security.JwtProvider;
import ddg.walking_rabbit.user.dto.KakaoResponseDto;
import ddg.walking_rabbit.user.entity.UserEntity;
import ddg.walking_rabbit.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Value("${kakao.auth.client}")
    private String clientId;

    @Value("${kakao.auth.redirect}")
    private String redirectUri;

    public KakaoResponseDto.OAuthToken getAccessToken(String code){

        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoResponseDto.OAuthToken> response = restTemplate.postForEntity(
                tokenUrl,
                request,
                KakaoResponseDto.OAuthToken.class
        );

        return response.getBody();
    }

    public KakaoResponseDto.KakaoProfile getUserInfo(String accessToken){

        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<KakaoResponseDto.KakaoProfile> response = restTemplate.exchange(
                userInfoUrl,
                HttpMethod.GET,
                request,
                KakaoResponseDto.KakaoProfile.class
        );

        return response.getBody();
    }

    public String login(UserEntity user){

        String username = user.getUsername();
        return jwtProvider.createToken(username);
    }
}
