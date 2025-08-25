package ddg.walking_rabbit.message.dto;

import ddg.walking_rabbit.global.domain.entity.ContentType;
import ddg.walking_rabbit.global.domain.entity.Role;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDto {
    private Long messageId;
    private Long conversationId;
    private Role role;
    private ContentType contentType;
    private String content;
    private List<String> keyword;
}
