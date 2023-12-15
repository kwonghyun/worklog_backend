package com.example.worklog.entity;

import com.example.worklog.entity.base.BaseTimeEntity;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@SQLDelete(sql = "UPDATE memo SET is_deleted = TRUE WHERE id = ?")
@Where(clause = "is_deleted = FALSE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    private String content;

//    @Nullable
//    private Integer order;

    @Nullable
    private LocalDate date;

    @Builder.Default
    private Boolean isDeleted = false;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "memo")
    @Builder.Default
    private List<SavedMemo> savedMemos = new ArrayList<>();

    public void updateContent(String content) {
        this.content = content;
    }

//    public void updateOrder(Integer order) {
//        this.order = order;
//    }
}
