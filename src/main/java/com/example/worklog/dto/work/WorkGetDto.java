package com.example.worklog.dto.work;

import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class WorkGetDto {
    private Long id;
    private String content;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deadline;
    private Category category;
    private WorkState state;
    private Integer order;

    public static WorkGetDto fromEntity(Work work) {
        WorkGetDto dto = new WorkGetDto();
        dto.setId(work.getId());
        dto.setTitle(work.getTitle());
        dto.setContent(work.getContent());
        dto.setDate(work.getDate());
        dto.setDeadline(work.getDeadline());
        dto.setCategory(work.getCategory());
        dto.setState(work.getState());
        dto.setOrder(work.getDisplayOrder());
        return dto;
    }
}
