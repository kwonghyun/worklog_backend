package com.example.worklog.dto.work;

import com.example.worklog.exception.CustomDateValidation;
import com.example.worklog.utils.Constant;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
@ToString
public class WorkGetParamDto {
    @CustomDateValidation
    @NotNull(message = Constant.DATE_NOT_VALID_MESSAGE)
    private String date;
}