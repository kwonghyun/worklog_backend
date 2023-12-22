package com.example.worklog.controller;

import com.example.worklog.dto.memo.MemoDisplayOrderPatchDto;
import com.example.worklog.dto.memo.MemoGetRequestParamDto;
import com.example.worklog.dto.ResourceResponseDto;
import com.example.worklog.dto.ResponseDto;
import com.example.worklog.dto.memo.MemoContentPatchDto;
import com.example.worklog.dto.memo.MemoPostDto;
import com.example.worklog.exception.SuccessCode;
import com.example.worklog.service.MemoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/memos")
public class MemoController {
    private final MemoService memoService;

    @PostMapping
    public ResponseEntity<ResponseDto> createMemo(
            @Valid @RequestBody MemoPostDto dto,
            Authentication auth
            ){
        memoService.createMemo(dto, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.MEMO_CREATED));
    }

    @GetMapping
    public ResponseEntity<ResourceResponseDto> readMemos(
            @Valid @ModelAttribute MemoGetRequestParamDto paramDto,
            Authentication auth
    ) {
        ResourceResponseDto responseDto = ResourceResponseDto.fromData(memoService.readMemos(paramDto, auth.getName()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @PatchMapping("/{memoId}/content")
    public ResponseEntity<ResponseDto> updateMemoContent(
            @PathVariable("memoId") Long memoId,
            @Valid @RequestBody MemoContentPatchDto dto,
            Authentication auth
    ) {
        log.info("memoId: {} 수정 요청", memoId);
        memoService.updateMemoContent(dto, memoId, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.MEMO_EDIT_SUCCESS));
    }

    @PatchMapping("/{memoId}/order")
    public ResponseEntity<ResponseDto> updateMemoOrder(
            @PathVariable("memoId") Long memoId,
            @Valid @RequestBody MemoDisplayOrderPatchDto dto,
            Authentication auth
    ) {
        log.info("memoId: {} 수정 요청", memoId);
        memoService.updateMemoDisplayOrder(dto, memoId, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.MEMO_EDIT_SUCCESS));
    }

    @DeleteMapping("/{memoId}")
    public ResponseEntity<ResponseDto> deleteMemo(
            @PathVariable("memoId") Long memoId,
            Authentication auth
    ) {
        memoService.deleteMemo(memoId, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.MEMO_DELETE_SUCCESS));
    }
}
