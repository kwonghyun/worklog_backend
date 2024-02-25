package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.exception.validation.EnumValueCheck;
import com.example.worklog.utils.Constants;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkStatePatchDto {
    @NotNull(message = Constants.WORK_STATE_NOT_BLANK)
    @EnumValueCheck(enumClass = WorkState.class, message = Constants.WORK_STATE_NOT_VALID_MESSAGE)
    private String state;
}
