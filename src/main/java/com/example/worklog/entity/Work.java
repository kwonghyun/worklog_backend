package com.example.worklog.entity;

import com.example.worklog.entity.base.BaseTimeEntity;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.WorkState;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Work extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDate date;
    private Category category;
    private WorkState state;
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "work")
    @Builder.Default
    private List<SavedWork> savedWorks = new ArrayList<>();

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateDate(LocalDate date) {
        this.date = date;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void updateState(WorkState state) {
        this.state = state;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
