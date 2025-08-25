package ddg.walking_rabbit.message.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ModelShortResponseDto {
    private String answer;
    private List<String> keyword;
}
