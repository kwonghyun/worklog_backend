package com.example.worklog.service;

import com.example.worklog.dto.CustomPage;
import com.example.worklog.dto.CustomPageable;
import com.example.worklog.dto.work.WorkPostDto;
import com.example.worklog.dto.work.WorkPutDto;
import com.example.worklog.dto.work.WorkSearchServiceDto;
import com.example.worklog.entity.User;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.Importance;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.WorkRepository;
import com.example.worklog.utils.Constants;
import com.example.worklog.utils.WorkCreateEvent;
import com.example.worklog.utils.WorkUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
// TODO 모니터링 툴 적용하기
public class WorkServiceImpl implements WorkService {
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
                WorkCreateEvent.builder().work(work).build()
        );
    }

    public List<Work> readWorks(LocalDate date, Long userId) {
        return workRepository.readWorksByParamsAndUser(date, userId);
    }

    public CustomPage<Work> searchWorks(WorkSearchServiceDto paramDto, CustomPageable pageable, Long userId) {
        return workRepository.findBySearchParams(
                paramDto, pageable, userId
        );
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
                    WorkUpdateEvent.builder().work(updatedWork).build()
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
                WorkUpdateEvent.builder().work(work).build()
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

//    public void updateWorkDisplayOrder(Integer targetOrder, Long workId, Long userId) {
//        Work work = getValidatedWorkByUserIdAndWorkId(userId, workId);
//
//        Integer currentOrder = work.getDisplayOrder();
//        if (currentOrder.equals(targetOrder)) return;
//        Integer lastOrder = workRepository.countDisplayOrder(work.getDate(), userId) - 1;
//        if (targetOrder > lastOrder) throw new CustomException(ErrorCode.WORK_ORDER_INVALID);
//
//
//        interface WorkUpdateOperation { void update(Work workToUpdate, int newDisplayOrder);}
//        WorkUpdateOperation updateOperation;
//        if (currentOrder > targetOrder) {
//            updateOperation = (workToUpdate, newDisplayOrder) -> workToUpdate.updateOrder(newDisplayOrder + 1);
//        } else {
//            updateOperation = (workToUpdate, newDisplayOrder) -> workToUpdate.updateOrder(newDisplayOrder - 1);
//        }
//
//        List<Work> worksToUpdateOrder = workRepository.readWorksToUpdateDisplayOrder(
//                work.getDate(),
//                userId,
//                currentOrder > targetOrder ? targetOrder : currentOrder + 1,
//                currentOrder > targetOrder ? currentOrder - 1 : targetOrder
//        );
//
//        int newOrder = currentOrder > targetOrder ? targetOrder : targetOrder - 1;
//        for (Work workToUpdate : worksToUpdateOrder) {
//            updateOperation.update(workToUpdate, newOrder);
//            newOrder += (currentOrder > targetOrder ? 1 : -1);
//        }
//
//        workRepository.saveAll(worksToUpdateOrder);
//    }

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
