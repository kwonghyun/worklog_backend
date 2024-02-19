package com.example.worklog.dto;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
@Slf4j
public class CustomPage<T> implements Page<T> {
    private final static int firstPage = 1;
    private final long total;
    private final CustomPageable pageable;
    private final List<T> content = new ArrayList<>();

    public <U> CustomPage(List<? extends U> content, CustomPageable pageable, long total) {
        this.content.addAll((List<? extends T>) content);
        this.pageable = pageable;
        this.total = pageable.toOptional().filter(it -> !this.content.isEmpty())
                .filter(it -> it.getOffset() + it.getPageSize() > total)
                .map(it -> it.getOffset() + this.content.size())
                .orElse(total);
    }

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getSize());
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public int getNumber() {
        return pageable.isPaged() ? pageable.getPageNumber() + 1: firstPage;
    }

    @Override
    public int getSize() {
        return pageable.isPaged() ? pageable.getPageSize() : content.size();
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public List<T> getContent() {
        return content;
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSort() {
        return pageable.getSort();
    }

    @Override
    public boolean isFirst() {
        return (getNumber() == firstPage) && (total != 0);
    }

    @Override
    public boolean isLast() {
        return getNumber() == getTotalPages();
    }

    @Override
    public boolean hasPrevious() {
        return (getNumber() > firstPage) && (getNumber() - 1 <= getTotalPages());
    }

    @Override
    public boolean hasNext() {
        return getNumber() < getTotalPages();
    }

    @Override
    public Pageable nextPageable() {
        return hasNext() ? pageable.next() : Pageable.unpaged();
    }

    @Override
    public Pageable previousPageable() {
        return hasPrevious() ? pageable.previousOrFirst() : Pageable.unpaged();
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new CustomPage<>(getConvertedContent(converter), this.pageable, this.total);
    }

    private <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {
        return this.content.stream().map(converter::apply).collect(Collectors.toList());
    }

    @Override
    public Iterator<T> iterator() {
        return this.content.iterator();
    }

    @Override
    public String toString() {
        return "CustomPage{" +
                "total=" + total +
                ", pageable=" + pageable +
                ", content=" + content +
                '}';
    }
}
