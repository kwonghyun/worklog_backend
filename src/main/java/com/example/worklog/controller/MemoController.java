package com.example.worklog.controller;

import com.example.worklog.dto.ResponseDto;
import com.example.worklog.dto.memo.MemoPostDto;
import com.example.worklog.exception.SuccessCode;
import com.example.worklog.service.MemoService;
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
            @RequestBody MemoPostDto dto,
            Authentication auth
            ){
        memoService.createMemo(dto, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.MEMO_CREATED));
    }

    @PatchMapping("/{memoId}")
    public ResponseEntity<ResponseDto> updateMemo(
            @PathVariable("memoId") Long memoId,
            @RequestBody String content,
            Authentication auth
    ) {
        log.info("memoId: {} 수정 요청", memoId);
        memoService.updateMemoContent(content, memoId, auth.getName());
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
