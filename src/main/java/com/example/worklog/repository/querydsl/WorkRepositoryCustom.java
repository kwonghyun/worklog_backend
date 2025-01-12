package com.example.worklog.repository.querydsl;

import com.example.worklog.dto.CustomPage;
import com.example.worklog.dto.CustomPageable;
import com.example.worklog.dto.work.WorkSearchReqParam;
import com.example.worklog.entity.Work;

public interface WorkRepositoryCustom {
    CustomPage<Work> findBySearchParams(WorkSearchReqParam dto, CustomPageable pageable, Long userId);
}
