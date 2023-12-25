package com.example.worklog.controller;

import com.example.worklog.dto.ResourceResponseDto;
import com.example.worklog.dto.ResponseDto;
import com.example.worklog.dto.work.*;
import com.example.worklog.exception.SuccessCode;
import com.example.worklog.service.WorkService;
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
@RequestMapping("works")
public class WorkController {
    private final WorkService workService;
    @PostMapping
    public ResponseEntity<ResponseDto> createWork(
            @Valid @RequestBody WorkPostDto dto,
            Authentication auth
    ){
        workService.createWork(dto, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_CREATED));
    }

    @GetMapping
    public ResponseEntity<ResourceResponseDto> readWorks(
            @Valid WorkGetRequestParamDto paramDto,
            Authentication auth
    ) {
        ResourceResponseDto responseDto = ResourceResponseDto.fromData(workService.readWorks(paramDto, auth.getName()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @PatchMapping("/{workId}/content")
    public ResponseEntity<ResponseDto> updateWorkContent(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkContentPatchDto dto,
            Authentication auth
    ) {
        workService.updateWorkContent(dto, workId, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/order")
    public ResponseEntity<ResponseDto> updateWorkOrder(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkDisplayOrderPatchDto dto,
            Authentication auth
    ) {
        workService.updateWorkDisplayOrder(dto, workId, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.MEMO_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/state")
    public ResponseEntity<ResponseDto> updateWorkState(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkStatePatchDto dto,
            Authentication auth
    ) {
        workService.updateWorkState(dto, workId, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/category")
    public ResponseEntity<ResponseDto> updateWorkCategory(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkCategoryPatchDto dto,
            Authentication auth
    ) {
        workService.updateWorkCategory(dto, workId, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }



    @DeleteMapping("/{workId}")
    public ResponseEntity<ResponseDto> deleteWork(
            @PathVariable("workId") Long workId,
            Authentication auth
    ) {
        workService.deleteWork(workId, auth.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_DELETE_SUCCESS));
    }

}
