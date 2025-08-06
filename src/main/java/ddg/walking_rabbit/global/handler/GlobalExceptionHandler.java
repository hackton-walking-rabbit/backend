package ddg.walking_rabbit.global.handler;

import ddg.walking_rabbit.global.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // (1) 해당 리소스 존재하지 않음
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleExist(EntityNotFoundException e) {
        return ErrorResponse.error(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // (2) 간단한 파라미터 오류, 비즈니스 로직 오류 등
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ErrorResponse.error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // (3) 기타 예외 처리 (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        e.printStackTrace();
        return ErrorResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
    }
}
