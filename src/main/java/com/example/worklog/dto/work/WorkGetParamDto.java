package com.example.worklog.dto.work;

import com.example.worklog.exception.DatePattern;
import com.example.worklog.utils.Constants;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkGetParamDto {
    @DatePattern
    @NotNull(message = Constants.DATE_NOT_VALID_MESSAGE)
    private String date;
}