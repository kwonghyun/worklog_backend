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
import java.util.List;

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

    public List<Memo> readMemos(MemoGetParamDto paramDto, String username) {
        User user = getValidatedUserByUsername(username);

        MemoGetRepoParamDto repoDto = MemoGetRepoParamDto.fromGetRequestDto(paramDto);

        return memoRepository.readMemosByParamsAndUser(repoDto, user);
    }

    public PageDto<MemoGetDto> searchMemos(MemoSearchParamDto paramDto, String username) {
        User user = getValidatedUserByUsername(username);

        MemoSearchRepoParamDto repoDto = MemoSearchRepoParamDto.fromGetRequestDto(paramDto);

        Page<Memo> pagedMemos = memoRepository.searchMemosByParamsAndUser(
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

        Integer currentOrder = memo.getDisplayOrder();
        Integer targetOrder = dto.getOrder();
        if (currentOrder.equals(targetOrder)) {
            return;
        }

        Integer lastOrder = memoRepository.countDisplayOrder(memo.getDate(), user) - 1;
        if (targetOrder > lastOrder) {
            throw new CustomException(ErrorCode.MEMO_ORDER_INVALID);
        }

        List<Memo> memosToUpdateOrder;
        memo.updateOrder(targetOrder);
        if (currentOrder > targetOrder) {
            memosToUpdateOrder = memoRepository.readMemosToUpdateDisplayOrder(memo.getDate(), user, targetOrder, currentOrder - 1);
            for (Memo memoToUpdate : memosToUpdateOrder) {
                memoToUpdate.updateOrder(memoToUpdate.getDisplayOrder() + 1);
            }
        } else {
            memosToUpdateOrder = memoRepository.readMemosToUpdateDisplayOrder(memo.getDate(), user, currentOrder + 1, targetOrder);
            for (Memo memoToUpdate : memosToUpdateOrder) {
                memoToUpdate.updateOrder(memoToUpdate.getDisplayOrder() - 1);
            }
        }
        memoRepository.saveAll(memosToUpdateOrder);
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
