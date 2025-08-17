package ddg.walking_rabbit.message.controller;

import ddg.walking_rabbit.global.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @PostMapping("/photo")
    public ResponseEntity<SuccessResponse<>>
}
