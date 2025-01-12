package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.utils.Constants;
import com.example.worklog.validation.EnumValueCheck;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkStatePatchReq {
    @NotNull(message = Constants.WORK_STATE_NOT_BLANK)
    @EnumValueCheck(enumClass = WorkState.class, message = Constants.WORK_STATE_NOT_VALID_MESSAGE)
    private String state;
}
