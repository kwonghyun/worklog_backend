package com.example.worklog.controller;

import com.example.worklog.dto.PageDto;
import com.example.worklog.dto.ResourceResponseDto;
import com.example.worklog.dto.ResponseDto;
import com.example.worklog.dto.memo.*;
import com.example.worklog.entity.User;
import com.example.worklog.exception.SuccessCode;
import com.example.worklog.service.MemoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/memos")
public class MemoController {
    private final MemoService memoService;

    @PostMapping
    public ResponseEntity<ResponseDto> createMemo(
            @Valid @RequestBody MemoPostDto dto,
            @AuthenticationPrincipal User user
            ){
        memoService.createMemo(dto, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.MEMO_CREATED));
    }

    @GetMapping
    public ResponseEntity<ResourceResponseDto> readMemos(
            @Valid @ModelAttribute MemoGetParamDto paramDto,
            @AuthenticationPrincipal User user
    ) {
        List<MemoGetDto> memos = memoService.readMemos(paramDto, user.getId());
        ResourceResponseDto responseDto = ResourceResponseDto.fromData(memos, memos.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<ResourceResponseDto> searchMemos(
            @Valid @ModelAttribute MemoSearchParamDto paramDto,
            Pageable pageable,
            @AuthenticationPrincipal User user
    ) {
        PageDto pageDto = memoService.searchMemos(paramDto, pageable, user.getId());
        ResourceResponseDto responseDto = ResourceResponseDto.fromData(pageDto, pageDto.getContent().size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @PatchMapping("/{memoId}/content")
    public ResponseEntity<ResponseDto> updateMemoContent(
            @PathVariable("memoId") Long memoId,
            @Valid @RequestBody MemoContentPatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        log.info("memoId: {} 수정 요청", memoId);
        memoService.updateMemoContent(dto, memoId, user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.MEMO_EDIT_SUCCESS));
    }

    @PatchMapping("/{memoId}/order")
    public ResponseEntity<ResponseDto> updateMemoOrder(
            @PathVariable("memoId") Long memoId,
            @Valid @RequestBody MemoDisplayOrderPatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        log.info("memoId: {} 수정 요청", memoId);
        memoService.updateMemoDisplayOrder(dto, memoId, user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.MEMO_EDIT_SUCCESS));
    }

    @DeleteMapping("/{memoId}")
    public ResponseEntity<ResponseDto> deleteMemo(
            @PathVariable("memoId") Long memoId,
            @AuthenticationPrincipal User user
    ) {
        memoService.deleteMemo(memoId, user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.MEMO_DELETE_SUCCESS));
    }
}
