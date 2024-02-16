package com.example.worklog.utils;

import com.example.worklog.entity.Work;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class WorkChangeEvent {
    private Work work;
}
