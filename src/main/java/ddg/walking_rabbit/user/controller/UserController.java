package ddg.walking_rabbit.user.controller;

import ddg.walking_rabbit.global.response.SuccessResponse;
import ddg.walking_rabbit.user.dto.SignUpRequestDto;
import ddg.walking_rabbit.user.dto.TokenResponseDto;
import ddg.walking_rabbit.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    public final UserService userService;

    @GetMapping("/exist")
    public ResponseEntity<SuccessResponse<Void>> existsByUsername(@RequestParam("username") String username) {
        userService.existsByUsername(username);
        return SuccessResponse.ok("아이디 사용이 가능합니다.");
    }

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<TokenResponseDto>> signUp(@RequestBody SignUpRequestDto requestDto) {
        String jwt = userService.signUp(requestDto);
        return SuccessResponse.onSuccess("jwt token이 생성되었습니다.", HttpStatus.CREATED, new TokenResponseDto(jwt));
    }

}
