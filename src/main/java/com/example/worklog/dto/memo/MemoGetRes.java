package com.example.worklog.dto.memo;

import com.example.worklog.entity.Memo;

import java.time.format.DateTimeFormatter;


public record MemoGetRes(
        Long id,
        String content,
        String date,
        Integer displayOrder
) {
    public static MemoGetRes fromEntity(Memo memo) {
        return new MemoGetRes(
                memo.getId(),
                memo.getContent(),
                memo.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                memo.getDisplayOrder()
        );
    }
}
