package ddg.walking_rabbit.calendar.controller;

import ddg.walking_rabbit.calendar.dto.CalendarResponseDto;
import ddg.walking_rabbit.calendar.service.CalendarService;
import ddg.walking_rabbit.global.domain.entity.UserEntity;
import ddg.walking_rabbit.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping
    public ResponseEntity<SuccessResponse<List<CalendarResponseDto>>> getMonthCalendar(
            @RequestParam Integer year,
            @RequestParam Integer month
    ){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CalendarResponseDto> responseDtos = calendarService.getCalendarChatRecords(user, year, month);
        return SuccessResponse.onSuccess("월별 캘린더를 성공적으로 반환하였습니다.", HttpStatus.OK, responseDtos);
    }
}
