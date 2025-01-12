package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.utils.Constants;
import com.example.worklog.validation.DatePattern;
import com.example.worklog.validation.EnumValueCheck;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@ToString
public class WorkSearchReqParam {
    @DatePattern
    private final String startDate;
    @DatePattern
    private final String endDate;
    @EnumValueCheck(enumClass = Category.class, message = Constants.CATEGORY_NOT_VALID_MESSAGE)
    private final String category;
    @EnumValueCheck(enumClass = WorkState.class, message = Constants.WORK_STATE_NOT_VALID_MESSAGE)
    private final String state;
    @Getter
    private final String keyword;

    public WorkSearchReqParam(String start_date, String end_date, String category, String state, String key) {
        this.startDate = start_date;
        this.endDate = end_date;
        this.category = category;
        this.state = state;
        this.keyword = key;
    }

    public LocalDate getStartDate() {
        return startDate != null ? LocalDate.parse(startDate) : null;
    }
    public LocalDate getEndDate() {
        return endDate != null ? LocalDate.parse(endDate) : null;
    }
    public Category getCategory() {
        return category != null ? Category.valueOf(category) : null;
    }
    public WorkState getState() {
        return state != null ? WorkState.valueOf(state) : null;
    }

}