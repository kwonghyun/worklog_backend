package com.example.worklog.controller;

import com.example.worklog.dto.CustomPage;
import com.example.worklog.dto.CustomPageable;
import com.example.worklog.dto.PageDto;
import com.example.worklog.dto.memo.*;
import com.example.worklog.entity.Memo;
import com.example.worklog.entity.User;
import com.example.worklog.dto.SuccessMessage;
import com.example.worklog.service.MemoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/memos")
public class MemoController {
    private final MemoService memoService;

    @PostMapping
    public ResponseEntity<SuccessMessage> createMemo(
            @Valid @RequestBody MemoPostDto dto,
            @AuthenticationPrincipal User user
            ){
        memoService.createMemo(dto, user);
        return ResponseEntity.ok(SuccessMessage.MEMO_CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MemoGetDto>> readMemos(
            @Valid @ModelAttribute MemoGetParamDto paramDto,
            @AuthenticationPrincipal User user
    ) {
        List<Memo> memos = memoService.readMemos(
                LocalDate.parse(paramDto.getDate()),
                user.getId()
        );
        List<MemoGetDto> dtos = memos.stream()
                .map(MemoGetDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search")
    public ResponseEntity<PageDto<MemoGetDto>> searchMemos(
            @Valid @ModelAttribute MemoSearchParamDto paramDto,
            CustomPageable pageable,
            @AuthenticationPrincipal User user
    ) {
        CustomPage<Memo> pagedMemos = memoService.searchMemos(
                MemoSearchServiceDto.from(paramDto),
                pageable,
                user.getId()
        );
        Page<MemoGetDto> pagedDtos
                = pagedMemos.map(MemoGetDto::fromEntity);
        PageDto<MemoGetDto> pageDto = PageDto.fromPage(pagedDtos);
        return ResponseEntity.ok(pageDto);
    }

    @PatchMapping("/{memoId}/content")
    public ResponseEntity<SuccessMessage> updateMemoContent(
            @PathVariable("memoId") Long memoId,
            @Valid @RequestBody MemoContentPatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        memoService.updateMemoContent(dto.getContent(), memoId, user.getId());
        return ResponseEntity.ok(SuccessMessage.MEMO_EDIT_SUCCESS);
    }

    @PatchMapping("/{memoId}/order")
    public ResponseEntity<SuccessMessage> updateMemoOrder(
            @PathVariable("memoId") Long memoId,
            @Valid @RequestBody MemoDisplayOrderPatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        log.info("memoId: {} 수정 요청", memoId);
        memoService.updateMemoDisplayOrder(dto.getOrder(), memoId, user.getId());
        return ResponseEntity.ok(SuccessMessage.MEMO_EDIT_SUCCESS);
    }

    @DeleteMapping("/{memoId}")
    public ResponseEntity<SuccessMessage> deleteMemo(
            @PathVariable("memoId") Long memoId,
            @AuthenticationPrincipal User user
    ) {
        memoService.deleteMemo(memoId, user.getId());
        return ResponseEntity.ok(SuccessMessage.MEMO_DELETE_SUCCESS);
    }
}
