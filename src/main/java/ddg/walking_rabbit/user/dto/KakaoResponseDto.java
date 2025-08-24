package ddg.walking_rabbit.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoResponseDto {

    @Getter
    public static class OAuthToken {
        @JsonProperty("access_token")
        private String accessToken;
    }

    @Getter
    public static class KakaoProfile {

        @JsonProperty("id")
        private Long id;
        @JsonProperty("connected_at")
        private String connectedAt;
        @JsonProperty("kakao_account")
        private KakaoAccount kaKaoAccount;

        @Getter
        public static class KakaoAccount{
            @JsonProperty("profile")
            private Profile profile;

            @Getter
            public static class Profile {
                @JsonProperty("nickname")
                private String nickname;
                @JsonProperty("profile_image_url")
                private String profileImageUrl;
            }
        }
    }


}
