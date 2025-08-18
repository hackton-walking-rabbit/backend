package ddg.walking_rabbit.message.dto;

import ddg.walking_rabbit.message.entity.ContentType;
import ddg.walking_rabbit.message.entity.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ChatResponseDto {
    private Long messageId;
    private Long conversationId;
    private Role role;
    private ContentType contentType;
    private String content;
}
