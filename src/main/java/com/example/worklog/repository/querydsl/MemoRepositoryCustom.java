package com.example.worklog.repository.querydsl;

import com.example.worklog.dto.CustomPage;
import com.example.worklog.dto.CustomPageable;
import com.example.worklog.dto.memo.MemoSearchServiceDto;
import com.example.worklog.entity.Memo;

public interface MemoRepositoryCustom {
    CustomPage<Memo> findBySearchParams(MemoSearchServiceDto dto, CustomPageable pageable, Long userId);
}
