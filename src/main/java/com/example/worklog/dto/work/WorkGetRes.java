package com.example.worklog.dto.work;

import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.utils.Constants;


public record WorkGetRes(
        Long id,
        String content,
        String title,
        String date,
        String deadline,
        Category category,
        WorkState state,
        Integer order
) {
    public static WorkGetRes from(Work work) {
        return new WorkGetRes(
                work.getId(),
                work.getContent(),
                work.getTitle(),
                work.getDate().format(Constants.DATE_FORMAT),
                work.getDeadline() != null ? work.getDeadline().format(Constants.DATE_TIME_FORMAT) : null,
                work.getCategory(),
                work.getState(),
                work.getDisplayOrder()
        );
    }
}
