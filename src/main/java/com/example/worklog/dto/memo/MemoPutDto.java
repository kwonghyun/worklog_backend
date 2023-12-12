package com.example.worklog.dto.memo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class MemoPutDto {
    private Long id;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotBlank(message = "날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

}
