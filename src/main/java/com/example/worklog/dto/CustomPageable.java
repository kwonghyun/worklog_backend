package com.example.worklog.dto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CustomPageable implements Pageable {
    private final int pageNumber;
    private final int pageSize;
    private final Sort sort;
    private final static int firstPage = 1;
    private final static int minSize = 1;
    private final static int maxSize = 100;
    private final static int defaultSize = 10;

    public CustomPageable(Integer page, Integer size, Sort sort) {
        this.pageNumber = (page == null || page < firstPage) ? firstPage : page;
        this.sort = sort == null ? Sort.unsorted() : sort;

        if (size == null) {
            this.pageSize = defaultSize;
        } else if (size < minSize) {
            this.pageSize = minSize;
        } else if (size > maxSize) {
            this.pageSize = maxSize;
        } else {
            this.pageSize = size;
        }
    }

    @Override
    public int getPageNumber() {
        return pageNumber - 1;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long getOffset() {
        return ((long) getPageNumber()) * ((long) pageSize);
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new CustomPageable(getPageNumber() + 1, getPageSize(), getSort());
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    public CustomPageable previous() {
        return pageNumber == firstPage ? this : new CustomPageable(pageNumber - 1, getPageSize(), getSort());
    }
    @Override
    public Pageable first() {
        return new CustomPageable(firstPage, getPageSize(), getSort());
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new CustomPageable(pageNumber, getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return pageNumber > firstPage;
    }
}
