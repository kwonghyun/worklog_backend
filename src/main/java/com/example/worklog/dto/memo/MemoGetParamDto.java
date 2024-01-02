package com.example.worklog.dto.memo;


import com.example.worklog.exception.CustomDateValid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
public class MemoGetParamDto {
    @CustomDateValid
    @NotNull(message = "조회할 날짜를 포함해 요청을 보내세요.")
    private String date;

    public MemoGetParamDto(
            @RequestParam(name = "date") String date
    ) {
        this.date = date;
    }
}