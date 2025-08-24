package ddg.walking_rabbit.chatRecord.dto;

import ddg.walking_rabbit.global.domain.entity.ContentType;
import ddg.walking_rabbit.global.domain.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDto {
    private Long messageId;
    private Role role;
    private ContentType contentType;
    private String content;
}
