package com.example.worklog.dto.memo;

import com.example.worklog.entity.Memo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class MemoGetDto {
    private Long id;
    private String content;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Integer displayOrder;

    public static MemoGetDto fromEntity(Memo memo) {
        MemoGetDto dto = new MemoGetDto();
        dto.setId(memo.getId());
        dto.setContent(memo.getContent());
        dto.setDate(memo.getDate());
        dto.setDisplayOrder(memo.getDisplayOrder());
        return dto;
    }
}
