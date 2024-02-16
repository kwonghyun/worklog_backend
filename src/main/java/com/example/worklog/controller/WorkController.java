package com.example.worklog.controller;

import com.example.worklog.dto.PageDto;
import com.example.worklog.dto.ResourceResponseDto;
import com.example.worklog.dto.ResponseDto;
import com.example.worklog.dto.work.*;
import com.example.worklog.entity.User;
import com.example.worklog.exception.SuccessCode;
import com.example.worklog.service.WorkService;
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
@RequestMapping("works")
public class WorkController {
    private final WorkService workService;
    @PostMapping
    public ResponseEntity<ResponseDto> createWork(
            @Valid @RequestBody WorkPostDto dto,
            @AuthenticationPrincipal User user
    ){
        workService.createWork(dto, user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_CREATED));
    }

    @GetMapping
    public ResponseEntity<ResourceResponseDto> readWorks(
            @Valid @ModelAttribute WorkGetParamDto paramDto,
            @AuthenticationPrincipal User user
    ) {
        List<WorkGetDto> works = workService.readWorks(paramDto, user.getId());
        ResourceResponseDto responseDto = ResourceResponseDto.fromData(works, works.size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @GetMapping("/search")
    public ResponseEntity<ResourceResponseDto> searchWorks(
            Pageable pageable,
            @Valid @ModelAttribute WorkSearchParamDto paramDto,
            @AuthenticationPrincipal User user
    ) {
        PageDto pageDto = workService.searchWorks(paramDto, pageable, user.getId());
        ResourceResponseDto responseDto = ResourceResponseDto.fromData(pageDto, pageDto.getContent().size());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDto);
    }

    @PutMapping("/{workId}")
    public ResponseEntity<ResponseDto> updateWork(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkPutDto dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWork(dto, workId, user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/title")
    public ResponseEntity<ResponseDto> updateWorkTitle(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkTitlePatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkTitle(dto, workId, user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }
    @PatchMapping("/{workId}/content")
    public ResponseEntity<ResponseDto> updateWorkContent(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkContentPatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkContent(dto, workId, user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/order")
    public ResponseEntity<ResponseDto> updateWorkOrder(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkDisplayOrderPatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkDisplayOrder(dto, workId, user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.MEMO_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/state")
    public ResponseEntity<ResponseDto> updateWorkState(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkStatePatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkState(dto, workId, user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/category")
    public ResponseEntity<ResponseDto> updateWorkCategory(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkCategoryPatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkCategory(dto, workId, user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
    }



    @DeleteMapping("/{workId}")
    public ResponseEntity<ResponseDto> deleteWork(
            @PathVariable("workId") Long workId,
            @AuthenticationPrincipal User user
    ) {
        workService.deleteWork(workId, user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_DELETE_SUCCESS));
    }

}
