package com.example.worklog.dto.memo;


import com.example.worklog.exception.validation.DatePattern;
import com.example.worklog.utils.Constants;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemoGetParamDto {
    @DatePattern
    @NotNull(message = Constants.DATE_NOT_VALID_MESSAGE)
    private String date;
}