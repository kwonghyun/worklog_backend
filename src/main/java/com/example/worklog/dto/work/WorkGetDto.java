package com.example.worklog.dto.work;

import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class WorkGetDto {
    private Long id;
    private String content;
    private String title;
    private String date;
    private String deadline;
    private Category category;
    private WorkState state;
    private Integer order;

    public static WorkGetDto fromEntity(Work work) {
        WorkGetDto dto = new WorkGetDto();
        dto.setId(work.getId());
        dto.setTitle(work.getTitle());
        dto.setContent(work.getContent());
        dto.setDate(work.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (work.getDeadline() != null) {
            dto.setDeadline(work.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        dto.setCategory(work.getCategory());
        dto.setState(work.getState());
        dto.setOrder(work.getDisplayOrder());
        return dto;
    }
}
