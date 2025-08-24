package ddg.walking_rabbit.chatRecord.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapResponseDto {
    private Long chatRecordId;
    private Double latitude;
    private Double longitude;
    private Long missionId;
}
