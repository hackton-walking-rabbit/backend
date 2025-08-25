package ddg.walking_rabbit.message.controller;

import ddg.walking_rabbit.global.response.SuccessResponse;
import ddg.walking_rabbit.message.dto.ChatRequestDto;
import ddg.walking_rabbit.message.dto.ChatResponseDto;
import ddg.walking_rabbit.message.dto.ChatStartDto;
import ddg.walking_rabbit.message.service.MessageService;
import ddg.walking_rabbit.global.domain.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping(value ="/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessResponse<ChatResponseDto>> startChat(
            @RequestPart("file") MultipartFile file,
            @RequestPart("meta") ChatStartDto chatStartDto
    ) {
        UserEntity user = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ChatResponseDto responseDto = messageService.startChat(user, file, chatStartDto);
        return SuccessResponse.onSuccess("사진에 대한 챗봇 응답이 생성되었습니다.", HttpStatus.CREATED, responseDto);
    }

    @PostMapping("/text")
    public ResponseEntity<SuccessResponse<ChatResponseDto>> doChat(@RequestBody ChatRequestDto requestDto) {
        UserEntity user = ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        ChatResponseDto responseDto = messageService.doChat(user, requestDto.getConversationId(), requestDto.getContent());
        return SuccessResponse.onSuccess("챗봇 응답이 생성되었습니다.", HttpStatus.CREATED, responseDto);
    }
}
