package ddg.walking_rabbit.global.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private Long kakaoId; // 고유키

    private String nickname; // kakao nickname -> 수정 가능

    private String profileImageUrl; // kakao 프사


}
