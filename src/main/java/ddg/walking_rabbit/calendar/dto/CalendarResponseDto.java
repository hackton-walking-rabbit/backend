package ddg.walking_rabbit.calendar.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CalendarResponseDto {
    private Integer day;
    private Integer recordSum;
    private List<ChatRecordDto> chatRecords;
}
