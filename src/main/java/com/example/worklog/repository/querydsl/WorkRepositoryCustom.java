package com.example.worklog.repository.querydsl;

import com.example.worklog.dto.work.WorkSearchServiceDto;
import com.example.worklog.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkRepositoryCustom {
    Page<Work> findBySearchParams(WorkSearchServiceDto dto, Pageable pageable, Long userId);
}
