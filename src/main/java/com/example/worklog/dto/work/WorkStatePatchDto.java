package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.utils.ValidationConstant;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkStatePatchDto {
    @NotNull(message = ValidationConstant.WORK_STATE_NOT_BLANK)
    private WorkState state;
}
