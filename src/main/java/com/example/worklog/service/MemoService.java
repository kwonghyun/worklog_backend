package com.example.worklog.service;

import com.example.worklog.dto.CustomPage;
import com.example.worklog.dto.CustomPageable;
import com.example.worklog.dto.memo.MemoPostDto;
import com.example.worklog.dto.memo.MemoSearchServiceDto;
import com.example.worklog.entity.Memo;
import com.example.worklog.entity.User;

import java.time.LocalDate;
import java.util.List;


public interface MemoService {
    public void createMemo(MemoPostDto dto, User user);
    public List<Memo> readMemos(LocalDate date, Long userId);
    public CustomPage<Memo> searchMemos(MemoSearchServiceDto serviceDto, CustomPageable pageable, Long userId);
    public void updateMemoContent(String content, Long memoId, Long userId);
    public void deleteMemo(Long memoId, Long userId);
    public void updateMemoDisplayOrder(Integer targetOrder, Long memoId, Long userId);
}
