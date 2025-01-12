package com.example.worklog.controller;

import com.example.worklog.dto.*;
import com.example.worklog.dto.memo.*;
import com.example.worklog.entity.Memo;
import com.example.worklog.entity.User;
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
    public ResponseEntity<SimpleMessageRes> createMemo(
            @Valid @RequestBody MemoPostReq dto,
            @AuthenticationPrincipal User user
            ){
        memoService.createMemo(dto, user);
        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.MEMO_CREATED));
    }

    @GetMapping
    public ResponseEntity<List<MemoGetRes>> readMemos(
            @Valid @ModelAttribute MemoGetReqParam paramDto,
            @AuthenticationPrincipal User user
    ) {
        List<Memo> memos = memoService.readMemos(
                LocalDate.parse(paramDto.getDate()),
                user.getId()
        );
        List<MemoGetRes> dtos = memos.stream()
                .map(MemoGetRes::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search")
    public ResponseEntity<PageDto<MemoGetRes>> searchMemos(
            @Valid @ModelAttribute MemoSearchReqParam paramDto,
            CustomPageable pageable,
            @AuthenticationPrincipal User user
    ) {
        CustomPage<Memo> pagedMemos = memoService.searchMemos(
                paramDto,
                pageable,
                user.getId()
        );
        Page<MemoGetRes> pagedDtos
                = pagedMemos.map(MemoGetRes::fromEntity);
        PageDto<MemoGetRes> pageDto = PageDto.from(pagedDtos);
        return ResponseEntity.ok(pageDto);
    }

    @PatchMapping("/{memoId}/content")
    public ResponseEntity<SimpleMessageRes> updateMemoContent(
            @PathVariable("memoId") Long memoId,
            @Valid @RequestBody MemoContentPatchReq dto,
            @AuthenticationPrincipal User user
    ) {
        memoService.updateMemoContent(dto.getContent(), memoId, user.getId());
        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.MEMO_EDIT_SUCCESS));
    }

    @PatchMapping("/{memoId}/order")
    public ResponseEntity<SimpleMessageRes> updateMemoOrder(
            @PathVariable("memoId") Long memoId,
            @Valid @RequestBody MemoDisplayOrderPatchReq dto,
            @AuthenticationPrincipal User user
    ) {
        log.info("memoId: {} 수정 요청", memoId);
        memoService.updateMemoDisplayOrder(dto.getOrder(), memoId, user.getId());
        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.MEMO_EDIT_SUCCESS));
    }

    @DeleteMapping("/{memoId}")
    public ResponseEntity<SimpleMessageRes> deleteMemo(
            @PathVariable("memoId") Long memoId,
            @AuthenticationPrincipal User user
    ) {
        memoService.deleteMemo(memoId, user.getId());
        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.MEMO_DELETE_SUCCESS));
    }
}
