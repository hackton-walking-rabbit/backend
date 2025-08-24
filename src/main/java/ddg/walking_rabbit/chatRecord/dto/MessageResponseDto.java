package ddg.walking_rabbit.chatRecord.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageResponseDto {
    private String mission;
    private List<MessageDto> messages;
}
