package ddg.walking_rabbit.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    private Long kakaoId;
    private String nickname;
    private String profileImageUrl;
}
