package com.example.worklog.controller;

import com.example.worklog.dto.PageDto;
import com.example.worklog.dto.ResourceResponseDto;
import com.example.worklog.dto.ResponseDto;
import com.example.worklog.dto.user.CustomUserDetails;
import com.example.worklog.dto.work.*;
import com.example.worklog.exception.SuccessCode;
import com.example.worklog.service.WorkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("works")
public class WorkController {
    private final WorkService workService;
    @PostMapping
    public ResponseEntity<ResponseDto> createWork(
            @Valid @RequestBody WorkPostDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        workService.createWork(dto, userDetails);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_CREATED));
    }

    @GetMapping
    public ResponseEntity<ResourceResponseDto> readWorks(
            @Valid @ModelAttribute WorkGetParamDto paramDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<WorkGetDto> works = workService.readWorks(paramDto, userDetails.getId());
        ResourceResponseDto responseDto = ResourceResponseDto.fromData(works, works.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<ResourceResponseDto> searchWorks(
            @Valid @ModelAttribute WorkSearchParamDto paramDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        PageDto pageDto = workService.searchWorks(paramDto, userDetails.getId());
        ResourceResponseDto responseDto = ResourceResponseDto.fromData(pageDto, pageDto.getContent().size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @PutMapping("/{workId}")
    public ResponseEntity<ResponseDto> updateWork(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkPutDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        workService.updateWork(dto, workId, userDetails.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/title")
    public ResponseEntity<ResponseDto> updateWorkTitle(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkTitlePatchDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        workService.updateWorkTitle(dto, workId, userDetails.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }
    @PatchMapping("/{workId}/content")
    public ResponseEntity<ResponseDto> updateWorkContent(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkContentPatchDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        workService.updateWorkContent(dto, workId, userDetails.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/order")
    public ResponseEntity<ResponseDto> updateWorkOrder(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkDisplayOrderPatchDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        workService.updateWorkDisplayOrder(dto, workId, userDetails.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.MEMO_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/state")
    public ResponseEntity<ResponseDto> updateWorkState(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkStatePatchDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        workService.updateWorkState(dto, workId, userDetails.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/category")
    public ResponseEntity<ResponseDto> updateWorkCategory(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkCategoryPatchDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        workService.updateWorkCategory(dto, workId, userDetails.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }



    @DeleteMapping("/{workId}")
    public ResponseEntity<ResponseDto> deleteWork(
            @PathVariable("workId") Long workId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        workService.deleteWork(workId, userDetails.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_DELETE_SUCCESS));
    }

}
