package ddg.walking_rabbit.chatRecord.controller;

import ddg.walking_rabbit.chatRecord.dto.MapResponseDto;
import ddg.walking_rabbit.chatRecord.dto.MessageResponseDto;
import ddg.walking_rabbit.chatRecord.dto.SaveResponseDto;
import ddg.walking_rabbit.chatRecord.service.ChatRecordService;
import ddg.walking_rabbit.global.domain.entity.UserEntity;
import ddg.walking_rabbit.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatRecords")
@RequiredArgsConstructor
public class ChatRecordController {

    private final ChatRecordService chatRecordService;

    @PostMapping("/save/{conversationId}")
    public ResponseEntity<SuccessResponse<SaveResponseDto>> saveChatRecord(@PathVariable Long conversationId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SaveResponseDto responseDto = chatRecordService.saveChatRecord(user, conversationId);
        return SuccessResponse.onSuccess("도감이 생성되었습니다.", HttpStatus.CREATED, responseDto);
    }

    @GetMapping("/{chatRecordId}")
    public ResponseEntity<SuccessResponse<MessageResponseDto>> getChatRecord(@PathVariable Long chatRecordId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MessageResponseDto responseDto = chatRecordService.getChatRecord(user, chatRecordId);
        return SuccessResponse.onSuccess("chatRecordId가 성공적으로 조회되었습니다.", HttpStatus.OK, responseDto);
    }

    @GetMapping("/map")
    public ResponseEntity<SuccessResponse<List<MapResponseDto>>> getChatRecordMap() {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<MapResponseDto> responseDtos = chatRecordService.getChatRecordMap(user);
        return SuccessResponse.onSuccess("도감이 조회되었습니다.", HttpStatus.OK, responseDtos);
    }
}
