package com.example.worklog.dto.memo;

import com.example.worklog.exception.CustomDateValidation;
import com.example.worklog.utils.Constant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemoPostDto {
    @NotBlank(message = Constant.CONTENT_NOT_BLANK)
    private String content;

    @CustomDateValidation
    @NotNull(message = Constant.DATE_NOT_VALID_MESSAGE)
    private String  date;
}
