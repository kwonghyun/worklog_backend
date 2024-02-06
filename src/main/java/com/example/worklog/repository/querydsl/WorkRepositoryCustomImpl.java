package com.example.worklog.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorkRepositoryCustomImpl implements WorkRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
}
