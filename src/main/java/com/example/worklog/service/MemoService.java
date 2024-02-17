package com.example.worklog.service;

import com.example.worklog.dto.PageDto;
import com.example.worklog.dto.memo.*;
import com.example.worklog.entity.Memo;
import com.example.worklog.entity.User;
import com.example.worklog.entity.enums.Importance;
import com.example.worklog.exception.CustomException;
import com.example.worklog.exception.ErrorCode;
import com.example.worklog.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;

    public void createMemo(MemoPostDto dto, User user) {
        LocalDate date = LocalDate.parse(dto.getDate());
        memoRepository.save(
                Memo.builder()
                        .content(dto.getContent())
                        .date(date)
                        .displayOrder(memoRepository.countDisplayOrder(date, user.getId()))
                        .importance(Importance.MID)
                        .user(user)
                        .build()
        );
    }

    public List<MemoGetDto> readMemos(LocalDate date, Long userId) {
        List<Memo> memos= memoRepository.readMemosByParamsAndUser(date, userId);

        return memos.stream()
                .map(memo -> MemoGetDto.fromEntity(memo))
                .collect(Collectors.toList());
    }

    public PageDto<MemoGetDto> searchMemos(MemoSearchServiceDto serviceDto, Pageable pageable, Long userId) {
        Page<Memo> pagedMemos = memoRepository.searchMemosByParamsAndUser(
                serviceDto, pageable, userId
        );

        Page<MemoGetDto> pageDto
                = pagedMemos.map(memo -> MemoGetDto.fromEntity(memo));

        return PageDto.fromPage(pageDto);
    }

    public void updateMemoContent(String content, Long memoId, Long userId) {
        Memo memo = getValidatedMemoByUserAndMemoId(userId, memoId);
        memo.updateContent(content);
        memoRepository.save(memo);
    }

    public void deleteMemo(Long memoId, Long userId) {
        Memo memo = getValidatedMemoByUserAndMemoId(userId, memoId);
        int lastOrder = memoRepository.countDisplayOrder(memo.getDate(), userId);
        int orderToDelete = memo.getDisplayOrder();
        if (orderToDelete < lastOrder) {
            List<Memo> memosToUpdateOrder
                    = memoRepository.readMemosToUpdateDisplayOrder(memo.getDate(), userId, orderToDelete + 1, lastOrder);
            for (Memo memoToUpdate : memosToUpdateOrder) {
                memoToUpdate.updateOrder(memoToUpdate.getDisplayOrder() - 1);
            }
            memoRepository.saveAll(memosToUpdateOrder);
        }
        memo.updateOrder(Integer.MIN_VALUE);
        memoRepository.delete(memo);
    }


    public void updateMemoDisplayOrder(Integer targetOrder, Long memoId, Long userId) {
        Memo memo = getValidatedMemoByUserAndMemoId(userId, memoId);

        Integer currentOrder = memo.getDisplayOrder();
        if (currentOrder.equals(targetOrder)) {
            return;
        }

        Integer lastOrder = memoRepository.countDisplayOrder(memo.getDate(), userId) - 1;
        if (targetOrder > lastOrder) {
            throw new CustomException(ErrorCode.MEMO_ORDER_INVALID);
        }

        List<Memo> memosToUpdateOrder;
        memo.updateOrder(targetOrder);
        if (currentOrder > targetOrder) {
            memosToUpdateOrder = memoRepository.readMemosToUpdateDisplayOrder(memo.getDate(), userId, targetOrder, currentOrder - 1);
            for (Memo memoToUpdate : memosToUpdateOrder) {
                memoToUpdate.updateOrder(memoToUpdate.getDisplayOrder() + 1);
            }
        } else {
            memosToUpdateOrder = memoRepository.readMemosToUpdateDisplayOrder(memo.getDate(), userId, currentOrder + 1, targetOrder);
            for (Memo memoToUpdate : memosToUpdateOrder) {
                memoToUpdate.updateOrder(memoToUpdate.getDisplayOrder() - 1);
            }
        }
        memoRepository.saveAll(memosToUpdateOrder);
    }

    private Memo getValidatedMemoByUserAndMemoId(Long userId, Long memoId) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMO_NOT_FOUND));

        if (!memo.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.MEMO_USER_NOT_MATCHED);
        } else {
            return memo;
        }
    }
}
