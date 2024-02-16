package com.example.worklog.dto.memo;

import com.example.worklog.exception.CustomDateValidation;
import com.example.worklog.utils.ValidationConstant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemoPostDto {
    @NotBlank(message = ValidationConstant.CONTENT_NOT_BLANK)
    private String content;

    @CustomDateValidation
    @NotNull(message = ValidationConstant.DATE_NOT_VALID_MESSAGE)
    private String  date;
}
