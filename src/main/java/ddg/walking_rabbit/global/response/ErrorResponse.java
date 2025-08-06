package ddg.walking_rabbit.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;

    public static ResponseEntity<ErrorResponse> error(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new ErrorResponse(httpStatus.value(), message), httpStatus);
    }

}
