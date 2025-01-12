package com.example.worklog.controller;

import com.example.worklog.dto.*;
import com.example.worklog.dto.work.*;
import com.example.worklog.entity.User;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.service.WorkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("works")
public class WorkController {
    private final WorkService workService;
    @PostMapping
    public ResponseEntity<SimpleMessageRes> createWork(
            @Valid @RequestBody WorkPostReq dto,
            @AuthenticationPrincipal User user
    ){
        workService.createWork(dto, user);
        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.WORK_CREATED));
    }

    @GetMapping
    public ResponseEntity<List<WorkGetRes>> readWorks(
            @Valid @ModelAttribute WorkGetParamReq paramDto,
            @AuthenticationPrincipal User user
    ) {
        List<Work> works = workService.readWorks(LocalDate.parse(paramDto.getDate()), user.getId());
        List<WorkGetRes> workGetRes = works.stream()
                .map(WorkGetRes::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(workGetRes);
    }

    @GetMapping("/search")
    public ResponseEntity<PageDto<WorkGetRes>> searchWorks(
            CustomPageable pageable,
            @Valid @ModelAttribute WorkSearchReqParam paramDto,
            @AuthenticationPrincipal User user
    ) {
        CustomPage<Work> pagedWorks = workService.searchWorks(
                paramDto,
                pageable,
                user.getId()
        );
        PageDto<WorkGetRes> pageDto = PageDto.from(
                pagedWorks.map(WorkGetRes::from)
        );
        return ResponseEntity.ok(pageDto);
    }

    @PutMapping("/{workId}")
    public ResponseEntity<SimpleMessageRes> updateWork(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkPutReq dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWork(dto, workId, user.getId());
        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.WORK_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/title")
    public ResponseEntity<SimpleMessageRes> updateWorkTitle(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkTitlePatchReq dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkTitle(dto.getTitle(), workId, user.getId());
        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.WORK_EDIT_SUCCESS));
    }
    @PatchMapping("/{workId}/content")
    public ResponseEntity<SimpleMessageRes> updateWorkContent(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkContentPatchReq dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkContent(dto.getContent(), workId, user.getId());
        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.WORK_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/order")
    public ResponseEntity<SimpleMessageRes> updateWorkOrder(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkDisplayOrderPatchReq dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkDisplayOrder(dto.getOrder(), workId, user.getId());
        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.MEMO_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/state")
    public ResponseEntity<SimpleMessageRes> updateWorkState(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkStatePatchReq dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkState(WorkState.from(dto.getState()), workId, user.getId());
        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.WORK_EDIT_SUCCESS));
    }

    @PatchMapping("/{workId}/category")
    public ResponseEntity<SimpleMessageRes> updateWorkCategory(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkCategoryPatchReq dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkCategory(Category.from(dto.getCategory()), workId, user.getId());
        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.WORK_EDIT_SUCCESS));
    }

    @DeleteMapping("/{workId}")
    public ResponseEntity<SimpleMessageRes> deleteWork(
            @PathVariable("workId") Long workId,
            @AuthenticationPrincipal User user
    ) {
        workService.deleteWork(workId, user.getId());
        return ResponseEntity.ok(
                SimpleMessageRes.from(SuccessMessage.WORK_DELETE_SUCCESS));
    }

}
