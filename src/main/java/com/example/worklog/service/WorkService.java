package com.example.worklog.service;

import com.example.worklog.dto.PageDto;
import com.example.worklog.dto.work.*;
import com.example.worklog.entity.User;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Importance;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.UserRepository;
import com.example.worklog.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkService {

    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    public void createWork(WorkPostDto dto, String username) {
        User user = getValidatedUserByUsername(username);
        LocalDate date = LocalDate.parse(dto.getDate());
        workRepository.save(
                Work.builder()
                        .content(dto.getContent())
                        .date(date)
                        .displayOrder(workRepository.countDisplayOrder(date, user))
                        .importance(Importance.MID)
                        .category(dto.getCategory())
                        .state(WorkState.IN_PROGRESS)
                        .user(user)
                        .build()
        );
    }

    public List<Work> readWorks(WorkGetParamDto paramDto, String username) {
        User user = getValidatedUserByUsername(username);

        WorkGetRepoParamDto repoDto = WorkGetRepoParamDto.fromGetRequestDto(paramDto);
        List<Work> works = workRepository.readWorksByParamsAndUser(
                repoDto, user
        );

        return works;
    }

    public PageDto<WorkGetDto> searchWorks(WorkSearchParamDto paramDto, String username) {
        User user = getValidatedUserByUsername(username);

        WorkSearchRepoParamDto repoDto = WorkSearchRepoParamDto.fromGetRequestDto(paramDto);
        log.info("category : {}",repoDto.getCategory() == null ? "null" : repoDto.getCategory().toString());
        log.info("state : {}",repoDto.getState() == null ? "null" : repoDto.getState().toString());
        Page<Work> pagedWorks = workRepository.searchWorksByParamsAndUser(
                repoDto, user,
                PageRequest.of(paramDto.getPageNum() - 1, paramDto.getPageSize())
        );

        Page<WorkGetDto> pageDto
                = pagedWorks.map(work -> WorkGetDto.fromEntity(work));

        return PageDto.fromPage(pageDto);
    }

    public void updateWorkContent(WorkContentPatchDto dto, Long workId, String username) {

        User user = getValidatedUserByUsername(username);
        Work work = getValidatedWorkByUserAndWorkId(user, workId);

        work.updateContent(dto.getContent());

        workRepository.save(work);
    }

    public void updateWorkState(WorkStatePatchDto dto, Long workId, String username) {
        User user = getValidatedUserByUsername(username);
        Work work = getValidatedWorkByUserAndWorkId(user, workId);

        work.updateState(dto.getState());

        workRepository.save(work);
    }

    public void updateWorkCategory(WorkCategoryPatchDto dto, Long workId, String username) {
        User user = getValidatedUserByUsername(username);
        Work work = getValidatedWorkByUserAndWorkId(user, workId);

        work.updateCategory(dto.getCategory());

        workRepository.save(work);
    }

    public void deleteWork(Long workId, String username) {
        User user = getValidatedUserByUsername(username);
        Work work = getValidatedWorkByUserAndWorkId(user, workId);

        workRepository.delete(work);
    }

    private User getValidatedUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Work getValidatedWorkByUserAndWorkId(User user, Long workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CustomException(ErrorCode.WORK_NOT_FOUND));

        if (!work.getUser().equals(user)) {
            throw new CustomException(ErrorCode.WORK_USER_NOT_MATCHED);
        } else {
            return work;
        }
    }

    public void updateWorkDisplayOrder(WorkDisplayOrderPatchDto dto, Long workId, String username) {
        User user = getValidatedUserByUsername(username);
        Work work = getValidatedWorkByUserAndWorkId(user, workId);

        Integer currentOrder = work.getDisplayOrder();
        Integer targetOrder = dto.getOrder();
        if (currentOrder.equals(targetOrder)) {
            return;
        }

        Integer lastOrder = workRepository.countDisplayOrder(work.getDate(), user) - 1;
        if (targetOrder > lastOrder) {
            throw new CustomException(ErrorCode.WORK_ORDER_INVALID);
        }

        List<Work> worksToUpdateOrder;
        work.updateOrder(targetOrder);
        if (currentOrder > targetOrder) {
            worksToUpdateOrder = workRepository.readWorksToUpdateDisplayOrder(work.getDate(), user, targetOrder, currentOrder - 1);
            for (Work workToUpdate : worksToUpdateOrder) {
                workToUpdate.updateOrder(workToUpdate.getDisplayOrder() + 1);
            }
        } else {
            worksToUpdateOrder = workRepository.readWorksToUpdateDisplayOrder(work.getDate(), user, currentOrder + 1, targetOrder);
            for (Work workToUpdate : worksToUpdateOrder) {
                workToUpdate.updateOrder(workToUpdate.getDisplayOrder() - 1);
            }
        }
        workRepository.saveAll(worksToUpdateOrder);
    }
}
