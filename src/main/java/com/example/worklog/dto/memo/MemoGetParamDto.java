package com.example.worklog.dto.memo;


import com.example.worklog.exception.CustomDateValidation;
import com.example.worklog.utils.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemoGetParamDto {
    @CustomDateValidation
    @NotNull(message = ValidationConstant.DATE_NOT_VALID_MESSAGE)
    private String date;
}