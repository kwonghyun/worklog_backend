package com.example.worklog.dto.work;

import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class WorkGetDto {
    private Long id;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Category category;
    private WorkState state;
    private Integer order;

    public static WorkGetDto fromEntity(Work work) {
        WorkGetDto dto = new WorkGetDto();
        dto.setId(work.getId());
        dto.setContent(work.getContent());
        dto.setDate(work.getDate());
        dto.setCategory(work.getCategory());
        dto.setState(work.getState());
        dto.setOrder(work.getDisplayOrder());
        return dto;
    }
}
