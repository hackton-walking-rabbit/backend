package ddg.walking_rabbit.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRequestDto {
    private Long conversationId;
    private String content;
}
