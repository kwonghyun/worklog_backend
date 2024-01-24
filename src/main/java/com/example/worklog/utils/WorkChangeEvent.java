package com.example.worklog.utils;

import com.example.worklog.entity.Work;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WorkChangeEvent {
    private Work work;
}
