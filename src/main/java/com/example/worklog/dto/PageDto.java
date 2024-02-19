package com.example.worklog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;
@Getter
@Setter
@ToString
public class PageDto<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private int numberOfElements;
    private long totalElements;
    private int totalPages;
    @JsonProperty("isFirstPage")
    private boolean isFirstPage;
    @JsonProperty("isLastPage")
    private boolean isLastPage;

    public static <T> PageDto<T> fromPage(Page<T> page) {
        PageDto<T> pageDto = new PageDto<>();
        pageDto.setContent(page.getContent());
        pageDto.setPageNumber(page.getNumber());
        pageDto.setPageSize(page.getSize());
        pageDto.setNumberOfElements(page.getNumberOfElements());
        pageDto.setTotalElements(page.getTotalElements());
        pageDto.setTotalPages(page.getTotalPages());
        pageDto.setFirstPage(page.isFirst());
        pageDto.setLastPage(page.isLast());
        return pageDto;
    }
}
