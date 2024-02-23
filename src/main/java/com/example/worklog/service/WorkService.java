package com.example.worklog.service;

import com.example.worklog.dto.CustomPage;
import com.example.worklog.dto.CustomPageable;
import com.example.worklog.dto.work.WorkPostDto;
import com.example.worklog.dto.work.WorkPutDto;
import com.example.worklog.dto.work.WorkSearchServiceDto;
import com.example.worklog.entity.User;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;

import java.time.LocalDate;
import java.util.List;


public interface WorkService {
    public void createWork(WorkPostDto dto, User user);
    public List<Work> readWorks(LocalDate date, Long userId);
    public CustomPage<Work> searchWorks(WorkSearchServiceDto paramDto, CustomPageable pageable, Long userId);
    public void updateWork(WorkPutDto dto, Long workId, Long userId);
    public void updateWorkTitle(String newTitle, Long workId, Long userId);
    public void updateWorkContent(String newContent, Long workId, Long userId);
    public void updateWorkState(WorkState newState, Long workId, Long userId);
    public void updateWorkCategory(Category newCategory, Long workId, Long userId);
    public void deleteWork(Long workId, Long userId);
    public void updateWorkDisplayOrder(Integer targetOrder, Long workId, Long userId);

}
