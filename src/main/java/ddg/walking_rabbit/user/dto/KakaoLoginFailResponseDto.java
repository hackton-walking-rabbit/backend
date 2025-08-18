package ddg.walking_rabbit.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class KakaoLoginFailResponseDto {
    private Long kakaoId;
    private String nickname;
    private String profileImageUrl;
}
