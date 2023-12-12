package com.example.worklog.service;

import com.example.worklog.dto.memo.MemoPostDto;
import com.example.worklog.dto.memo.MemoPutDto;
import com.example.worklog.entity.Memo;
import com.example.worklog.entity.User;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.MemoRepository;
import com.example.worklog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                        .date(dto.getDate())
                        .user(user)
                        .build()
        );
    }

    public void updateMemo(MemoPutDto dto, Long memoId, String username) {
        if (!dto.getId().equals(memoId)) {
            throw new CustomException(ErrorCode.ERROR_BAD_REQUEST);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Memo memo = memoRepository.findById(dto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NOT_FOUND));

        if (!memo.getUser().equals(user)) {
            throw new CustomException(ErrorCode.MEMO_USER_NOT_MATCHED);
        }

        memo.updateContent(dto.getContent());
        memo.updateDate(dto.getDate());

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
