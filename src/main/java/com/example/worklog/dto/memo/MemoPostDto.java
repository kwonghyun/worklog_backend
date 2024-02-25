package com.example.worklog.dto.memo;

import com.example.worklog.exception.validation.DatePattern;
import com.example.worklog.utils.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemoPostDto {
    @NotBlank(message = Constants.CONTENT_NOT_BLANK)
    private String content;

    @DatePattern
    @NotNull(message = Constants.DATE_NOT_VALID_MESSAGE)
    private String  date;
}
