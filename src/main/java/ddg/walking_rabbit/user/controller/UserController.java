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


}
