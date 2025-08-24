package ddg.walking_rabbit.user.service;

import ddg.walking_rabbit.global.domain.entity.UserEntity;
import ddg.walking_rabbit.global.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;

    public Optional<UserEntity> findUserByKakaoId(Long kakaoId) {
        return userRepository.findByKakaoId(kakaoId);
    }


    @Transactional
    public String signUp(Long kakaoId, String nickname, String profileImageUrl) {

        UserEntity user = UserEntity.builder()
                .kakaoId(kakaoId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();

        userRepository.save(user);

        return authService.login(user);
    }
}
