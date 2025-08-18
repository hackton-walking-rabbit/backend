package ddg.walking_rabbit.message.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatStartDto {
    private Long missionId;
    private Double latitude;
    private Double longitude;
}
