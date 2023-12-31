package com.example.worklog.dto.work;

import com.example.worklog.exception.CustomDateValid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
public class WorkGetParamDto {
    @CustomDateValid
    @NotNull
    private String date;

    public WorkGetParamDto(
            @RequestParam(name = "date") String date
    ) {
        this.date = date;
    }
}