package com.example.worklog.dto.memo;

import com.example.worklog.entity.Memo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class MemoGetDto {
    private Long id;
    private String content;
    private String date;
    private Integer displayOrder;

    public static MemoGetDto fromEntity(Memo memo) {
        MemoGetDto dto = new MemoGetDto();
        dto.setId(memo.getId());
        dto.setContent(memo.getContent());
        dto.setDate(memo.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dto.setDisplayOrder(memo.getDisplayOrder());
        return dto;
    }
}
