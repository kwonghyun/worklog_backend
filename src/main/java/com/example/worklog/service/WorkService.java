package com.example.worklog.service;

import com.example.worklog.dto.PageDto;
import com.example.worklog.dto.work.*;
import com.example.worklog.entity.User;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.Importance;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.WorkRepository;
import com.example.worklog.utils.Constants;
import com.example.worklog.utils.WorkChangeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
// TODO 모니터링 툴 적용하기
public class WorkService {
    private final WorkRepository workRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void createWork(WorkPostDto dto, User user) {
        LocalDate date = LocalDate.parse(dto.getDate());
        LocalDateTime deadline = dto.getDeadline() == null ?
                null : LocalDateTime.parse(dto.getDeadline(), Constants.DATE_TIME_FORMAT);
        Work work = workRepository.save(
                Work.builder()
                        .title(dto.getTitle())
                        .content(dto.getContent())
                        .date(date)
                        .deadline(deadline)
                        .displayOrder(workRepository.countDisplayOrder(date, user.getId()))
                        .importance(Importance.MID)
                        .category(Category.from(dto.getCategory()))
                        .state(WorkState.IN_PROGRESS)
                        .user(user)
                        .build()
        );

        applicationEventPublisher.publishEvent(
                WorkChangeEvent.builder().work(work).build()
        );
    }

    public List<WorkGetDto> readWorks(LocalDate date, Long userId) {
        List<Work> works = workRepository.readWorksByParamsAndUser(date, userId);

        return works.stream()
                .map(work -> WorkGetDto.fromEntity(work))
                .collect(Collectors.toList());
    }

    public Work findOne(Long id) {
        return workRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.WORK_NOT_FOUND));
    }

    public PageDto<WorkGetDto> searchWorks(WorkSearchServiceDto paramDto, Pageable pageable, Long userId) {
        Page<Work> pagedWorks = workRepository.findBySearchParams(
                paramDto, pageable, userId
        );

        Page<WorkGetDto> pageDto
                = pagedWorks.map(work -> WorkGetDto.fromEntity(work));

        return PageDto.fromPage(pageDto);
    }


    public void updateWork(WorkPutDto dto, Long workId, Long userId) {

        Work work = getValidatedWorkByUserIdAndWorkId(userId, workId);

        LocalDateTime deadline = dto.getDeadline() == null ?
                null : LocalDateTime.parse(dto.getDeadline(), Constants.DATE_TIME_FORMAT);

        work.updateTitle(dto.getTitle());
        work.updateContent(dto.getContent());
        work.updateDeadline(deadline);
        work.updateCategory(Category.from(dto.getCategory()));
        work.updateState(WorkState.from(dto.getState()));
        work.updateNoticed(false);

        Work updatedWork = workRepository.save(work);

        applicationEventPublisher.publishEvent(
                    WorkChangeEvent.builder().work(updatedWork).build()
        );
    }

    public void updateWorkTitle(String newTitle, Long workId, Long userId) {
        Work work = getValidatedWorkByUserIdAndWorkId(userId, workId);
        work.updateTitle(newTitle);
        workRepository.save(work);
    }

    public void updateWorkContent(String newContent, Long workId, Long userId) {
        Work work = getValidatedWorkByUserIdAndWorkId(userId, workId);
        work.updateContent(newContent);
        workRepository.save(work);
    }

    public void updateWorkState(WorkState newState, Long workId, Long userId) {
        Work work = getValidatedWorkByUserIdAndWorkId(userId, workId);
        work.updateState(newState);
        workRepository.save(work);
    }

    public void updateWorkCategory(Category newCategory, Long workId, Long userId) {
        Work work = getValidatedWorkByUserIdAndWorkId(userId, workId);
        work.updateCategory(newCategory);
        workRepository.save(work);
    }

    public void deleteWork(Long workId, Long userId) {
        Work work = getValidatedWorkByUserIdAndWorkId(userId, workId);

        int lastOrder = workRepository.countDisplayOrder(work.getDate(), userId);
        int orderToDelete = work.getDisplayOrder();
        if (orderToDelete < lastOrder) {
            List<Work> worksToUpdateOrder = workRepository.readWorksToUpdateDisplayOrder(work.getDate(), userId, orderToDelete + 1, lastOrder);
            for (Work workToUpdate : worksToUpdateOrder) {
                workToUpdate.updateOrder(workToUpdate.getDisplayOrder() - 1);
            }
            workRepository.saveAll(worksToUpdateOrder);
        }
        work.updateOrder(Integer.MIN_VALUE);
        workRepository.delete(work);

        applicationEventPublisher.publishEvent(
                WorkChangeEvent.builder().work(work).build()
        );
    }

    public void updateWorkDisplayOrder(Integer targetOrder, Long workId, Long userId) {
        Work work = getValidatedWorkByUserIdAndWorkId(userId, workId);

        Integer currentOrder = work.getDisplayOrder();
        if (currentOrder.equals(targetOrder)) return;

        Integer lastOrder = workRepository.countDisplayOrder(work.getDate(), userId) - 1;
        if (targetOrder > lastOrder) {
            throw new CustomException(ErrorCode.WORK_ORDER_INVALID);
        }

        List<Work> worksToUpdateOrder;
        work.updateOrder(targetOrder);
        if (currentOrder > targetOrder) {
            worksToUpdateOrder = workRepository.readWorksToUpdateDisplayOrder(work.getDate(), userId, targetOrder, currentOrder - 1);
            for (Work workToUpdate : worksToUpdateOrder) {
                workToUpdate.updateOrder(workToUpdate.getDisplayOrder() + 1);
            }
        } else {
            worksToUpdateOrder = workRepository.readWorksToUpdateDisplayOrder(work.getDate(), userId, currentOrder + 1, targetOrder);
            for (Work workToUpdate : worksToUpdateOrder) {
                workToUpdate.updateOrder(workToUpdate.getDisplayOrder() - 1);
            }
        }
        workRepository.saveAll(worksToUpdateOrder);
    }

    private Work getValidatedWorkByUserIdAndWorkId(Long userId, Long workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CustomException(ErrorCode.WORK_NOT_FOUND));

        if (!work.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.WORK_USER_NOT_MATCHED);
        } else {
            return work;
        }
    }
}
