package com.example.worklog.service;

import com.example.worklog.dto.work.WorkCategoryPatchDto;
import com.example.worklog.dto.work.WorkContentPatchDto;
import com.example.worklog.dto.work.WorkPostDto;
import com.example.worklog.dto.work.WorkStatePatchDto;
import com.example.worklog.entity.User;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.UserRepository;
import com.example.worklog.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class WorkService {

    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    public void createWork(WorkPostDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        workRepository.save(
                Work.builder()
                        .content(dto.getContent())
                        .date(LocalDate.parse(dto.getDate()))
                        .category(dto.getCategory())
                        .state(WorkState.IN_PROGRESS)
                        .isDeleted(false)
                        .user(user)
                        .build()
        );
    }

    public void updateWorkContent(WorkContentPatchDto dto, Long workId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CustomException(ErrorCode.WORK_NOT_FOUND));

        if (!work.getUser().equals(user)) {
            throw new CustomException(ErrorCode.WORK_USER_NOT_MATCHED);
        }

        work.updateContent(dto.getContent());

        workRepository.save(work);
    }

    public void updateWorkState(WorkStatePatchDto dto, Long workId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CustomException(ErrorCode.WORK_NOT_FOUND));

        if (!work.getUser().equals(user)) {
            throw new CustomException(ErrorCode.WORK_USER_NOT_MATCHED);
        }

        work.updateState(dto.getState());

        workRepository.save(work);
    }

    public void updateWorkCategory(WorkCategoryPatchDto dto, Long workId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CustomException(ErrorCode.WORK_NOT_FOUND));

        if (!work.getUser().equals(user)) {
            throw new CustomException(ErrorCode.WORK_USER_NOT_MATCHED);
        }

        work.updateCategory(dto.getCategory());

        workRepository.save(work);
    }

    public void deleteWork(Long workId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new CustomException(ErrorCode.WORK_NOT_FOUND));

        if (!work.getUser().equals(user)) {
            throw new CustomException(ErrorCode.WORK_USER_NOT_MATCHED);
        }

        work.delete();
        workRepository.save(work);
    }

}
