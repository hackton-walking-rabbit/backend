package ddg.walking_rabbit.user.service;

import com.google.cloud.storage.*;
import ddg.walking_rabbit.global.domain.entity.UserEntity;
import ddg.walking_rabbit.global.domain.repository.UserRepository;
import ddg.walking_rabbit.user.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final Storage storage;

    @Value("${gcp.storage.bucket.name}")
    private String bucketName;

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

    public UserInfoDto getUserInfo(UserEntity user){
        UserInfoDto result = new UserInfoDto();
        result.setNickname(user.getNickname());
        result.setProfileImageUrl(user.getProfileImageUrl());
        return result;
    }

    public void updateNickname(UserEntity user, String nickname) {
        user.setNickname(nickname);
        userRepository.save(user);
    }

    public void updateProfileImage(UserEntity user, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            user.setProfileImageUrl(null);
            userRepository.save(user);
        }

        String newFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, newFilename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        try {
            Blob blob = storage.create(blobInfo, file.getBytes());
            if (blob == null || blob.getSize() == 0) {
                throw new IllegalArgumentException("GCS 업로드 중 오류가 발생했습니다.");
            }
        } catch(IOException | StorageException e){
            throw new IllegalArgumentException("GCS 업로드 중 오류가 발생했습니다.");
        }

        String publicUrl = String.format("https://storage.googleapis.com/%s/%s",
                bucketName, URLEncoder.encode(newFilename, StandardCharsets.UTF_8));

        user.setProfileImageUrl(publicUrl);
        userRepository.save(user);
    }
}
