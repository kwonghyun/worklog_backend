package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class WorkPostDto {
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "업무 유형을 선택해주세요.")
    private Category category;
}
