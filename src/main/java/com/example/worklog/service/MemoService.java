package com.example.worklog.service;

import com.example.worklog.dto.memo.*;
import com.example.worklog.dto.PageDto;
import com.example.worklog.entity.Memo;
import com.example.worklog.entity.User;
import com.example.worklog.entity.enums.Importance;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.MemoRepository;
import com.example.worklog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final UserRepository userRepository;
    private final MemoRepository memoRepository;

    public void createMemo(MemoPostDto dto, String username) {
        User user = getValidatedUserByUsername(username);
        LocalDate date = LocalDate.parse(dto.getDate());
        memoRepository.save(
                Memo.builder()
                        .content(dto.getContent())
                        .date(date)
                        .displayOrder(memoRepository.countDisplayOrder(date, user))
                        .importance(Importance.MID)
                        .user(user)
                        .build()
        );
    }

    public PageDto<MemoGetDto> readMemos(MemoGetRequestParamDto paramDto, String username) {
        User user = getValidatedUserByUsername(username);

        MemoGetRepoParamDto repoDto = MemoGetRepoParamDto.fromGetRequestDto(paramDto);

        Page<Memo> pagedMemos = memoRepository.readMemosByParamsAndUser(
                repoDto, user,
                PageRequest.of(paramDto.getPageNum() - 1, paramDto.getPageSize())
        );

        Page<MemoGetDto> pageDto
                = pagedMemos.map(memo -> MemoGetDto.fromEntity(memo));

        return PageDto.fromPage(pageDto);
    }

    public void updateMemoContent(MemoContentPatchDto dto, Long memoId, String username) {
        User user = getValidatedUserByUsername(username);
        Memo memo = getValidatedMemoByUserAndMemoId(user, memoId);

        memo.updateContent(dto.getContent());
        memoRepository.save(memo);
    }

    public void deleteMemo(Long memoId, String username) {
        User user = getValidatedUserByUsername(username);
        Memo memo = getValidatedMemoByUserAndMemoId(user, memoId);

        memoRepository.delete(memo);
    }


    public void updateMemoDisplayOrder(MemoDisplayOrderPatchDto dto, Long memoId, String username) {
        User user = getValidatedUserByUsername(username);
        Memo memo = getValidatedMemoByUserAndMemoId(user, memoId);
    }

    private User getValidatedUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Memo getValidatedMemoByUserAndMemoId(User user, Long memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NOT_FOUND));

        if (!memo.getUser().equals(user)) {
            throw new CustomException(ErrorCode.MEMO_USER_NOT_MATCHED);
        } else {
            return memo;
        }
    }
}
