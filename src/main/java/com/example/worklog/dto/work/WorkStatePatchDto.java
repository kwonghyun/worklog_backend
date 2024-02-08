package com.example.worklog.dto.work;

import com.example.worklog.entity.enums.WorkState;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WorkStatePatchDto {
    @NotNull(message = "올바른 업무 상태를 입력해주세요.")
    private WorkState state;
}
