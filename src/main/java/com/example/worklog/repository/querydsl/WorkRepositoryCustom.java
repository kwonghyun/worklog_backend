package com.example.worklog.repository.querydsl;

import com.example.worklog.dto.work.WorkSearchRepoParamDto;
import com.example.worklog.entity.User;
import com.example.worklog.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkRepositoryCustom {
    Page<Work> findBySearchParams(WorkSearchRepoParamDto dto, Long userId, Pageable pageable);
}
