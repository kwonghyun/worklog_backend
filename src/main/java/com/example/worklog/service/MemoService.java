package com.example.worklog.service;

import com.example.worklog.dto.memo.MemoContentPatchDto;
import com.example.worklog.dto.memo.MemoPostDto;
import com.example.worklog.entity.Memo;
import com.example.worklog.entity.User;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.MemoRepository;
import com.example.worklog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final UserRepository userRepository;
    private final MemoRepository memoRepository;
    public void createMemo(MemoPostDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        memoRepository.save(
                Memo.builder()
                        .content(dto.getContent())
                        .date(LocalDate.parse(dto.getDate()))
                        .isDeleted(false)
                        .user(user)
                        .build()
        );
    }

    public void updateMemoContent(MemoContentPatchDto dto, Long memoId, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NOT_FOUND));

        if (!memo.getUser().equals(user)) {
            throw new CustomException(ErrorCode.MEMO_USER_NOT_MATCHED);
        }

        memo.updateContent(dto.getContent());

        memoRepository.save(memo);
    }

    public void deleteMemo(Long memoId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NOT_FOUND));

        if (!memo.getUser().equals(user)) {
            throw new CustomException(ErrorCode.MEMO_USER_NOT_MATCHED);
        }

        memo.delete();
        memoRepository.save(memo);
    }
}
