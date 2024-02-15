package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.exception.CustomDateValidation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkSearchParamDto {
    @CustomDateValidation
    private String startDate;
    @CustomDateValidation
    private String endDate;
    private String key;
    private Category category;
    private WorkState state;
}