package com.example.worklog.controller;

import com.example.worklog.dto.CustomPage;
import com.example.worklog.dto.CustomPageable;
import com.example.worklog.dto.PageDto;
import com.example.worklog.dto.work.*;
import com.example.worklog.entity.User;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.dto.SuccessMessage;
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
    public ResponseEntity<SuccessMessage> createWork(
            @Valid @RequestBody WorkPostDto dto,
            @AuthenticationPrincipal User user
    ){
        workService.createWork(dto, user);
        return ResponseEntity.ok(SuccessMessage.WORK_CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WorkGetDto>> readWorks(
            @Valid @ModelAttribute WorkGetParamDto paramDto,
            @AuthenticationPrincipal User user
    ) {
        List<Work> works = workService.readWorks(LocalDate.parse(paramDto.getDate()), user.getId());
        List<WorkGetDto> workGetDtos = works.stream()
                .map(WorkGetDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(workGetDtos);
    }

    @GetMapping("/search")
    public ResponseEntity<PageDto<WorkGetDto>> searchWorks(
            CustomPageable pageable,
            @Valid @ModelAttribute WorkSearchParamDto paramDto,
            @AuthenticationPrincipal User user
    ) {
        CustomPage<Work> pagedWorks = workService.searchWorks(
                WorkSearchServiceDto.from(paramDto),
                pageable,
                user.getId()
        );
        PageDto<WorkGetDto> pageDto = PageDto.fromPage(
                pagedWorks.map(WorkGetDto::fromEntity)
        );
        return ResponseEntity.ok(pageDto);
    }

    @PutMapping("/{workId}")
    public ResponseEntity<SuccessMessage> updateWork(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkPutDto dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWork(dto, workId, user.getId());
        return ResponseEntity.ok(SuccessMessage.WORK_EDIT_SUCCESS);
    }

    @PatchMapping("/{workId}/title")
    public ResponseEntity<SuccessMessage> updateWorkTitle(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkTitlePatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkTitle(dto.getTitle(), workId, user.getId());
        return ResponseEntity.ok(SuccessMessage.WORK_EDIT_SUCCESS);
    }
    @PatchMapping("/{workId}/content")
    public ResponseEntity<SuccessMessage> updateWorkContent(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkContentPatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkContent(dto.getContent(), workId, user.getId());
        return ResponseEntity.ok(SuccessMessage.WORK_EDIT_SUCCESS);
    }

    @PatchMapping("/{workId}/order")
    public ResponseEntity<SuccessMessage> updateWorkOrder(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkDisplayOrderPatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkDisplayOrder(dto.getOrder(), workId, user.getId());
        return ResponseEntity.ok(SuccessMessage.MEMO_EDIT_SUCCESS);
    }

    @PatchMapping("/{workId}/state")
    public ResponseEntity<SuccessMessage> updateWorkState(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkStatePatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkState(WorkState.from(dto.getState()), workId, user.getId());
        return ResponseEntity.ok(SuccessMessage.WORK_EDIT_SUCCESS);
    }

    @PatchMapping("/{workId}/category")
    public ResponseEntity<SuccessMessage> updateWorkCategory(
            @PathVariable("workId") Long workId,
            @Valid @RequestBody WorkCategoryPatchDto dto,
            @AuthenticationPrincipal User user
    ) {
        workService.updateWorkCategory(Category.from(dto.getCategory()), workId, user.getId());
        return ResponseEntity.ok(SuccessMessage.WORK_EDIT_SUCCESS);
    }

    @DeleteMapping("/{workId}")
    public ResponseEntity<SuccessMessage> deleteWork(
            @PathVariable("workId") Long workId,
            @AuthenticationPrincipal User user
    ) {
        workService.deleteWork(workId, user.getId());
        return ResponseEntity.ok(SuccessMessage.WORK_DELETE_SUCCESS);
    }

}
