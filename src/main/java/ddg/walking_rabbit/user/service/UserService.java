package ddg.walking_rabbit.user.service;

import ddg.walking_rabbit.user.dto.SignUpRequestDto;
import ddg.walking_rabbit.user.entity.UserEntity;
import ddg.walking_rabbit.user.repository.UserRepository;
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

    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    @Transactional
    public String signUp(SignUpRequestDto requestDto) {

        UserEntity user = UserEntity.builder()
                .kakaoId(requestDto.getKakaoId())
                .nickname(requestDto.getNickname())
                .profileImageUrl(requestDto.getProfileImageUrl())
                .username(requestDto.getUsername())
                .build();
        userRepository.save(user);

        return authService.login(user);
    }
}
