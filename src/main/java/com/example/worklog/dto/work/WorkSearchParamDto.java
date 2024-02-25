package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.exception.validation.DatePattern;
import com.example.worklog.exception.validation.EnumValueCheck;
import com.example.worklog.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkSearchParamDto {
    @DatePattern
    private String startDate;
    @DatePattern
    private String endDate;
    @EnumValueCheck(enumClass = Category.class, message = Constants.CATEGORY_NOT_VALID_MESSAGE)
    private String category;
    @EnumValueCheck(enumClass = WorkState.class, message = Constants.WORK_STATE_NOT_VALID_MESSAGE)
    private String state;
    private String keyword;

    public WorkSearchParamDto(String start_date, String end_date, String category, String state, String key) {
        this.startDate = start_date;
        this.endDate = end_date;
        this.category = category;
        this.state = state;
        this.keyword = key;
    }
}