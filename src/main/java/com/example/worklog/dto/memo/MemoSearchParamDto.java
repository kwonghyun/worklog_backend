package com.example.worklog.dto.memo;

import com.example.worklog.exception.CustomDateValidation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemoSearchParamDto {
    @CustomDateValidation
    private String startDate;
    @CustomDateValidation
    private String endDate;
    private String keyword;
}