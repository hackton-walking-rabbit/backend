package ddg.walking_rabbit.message.dto;

import lombok.Getter;

@Getter
public class ModelResponseDto {
    private String answer;
    private String title;
    private boolean isSuccess;
}
