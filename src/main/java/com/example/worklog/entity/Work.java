package com.example.worklog.entity;

import com.example.worklog.entity.base.BaseTimeEntity;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.Importance;
import com.example.worklog.entity.enums.WorkState;
import com.example.worklog.utils.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@SQLDelete(sql = "UPDATE work SET is_deleted = TRUE WHERE id = ?")
@Where(clause = "is_deleted = FALSE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Work extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private LocalDate date;

    private LocalDateTime deadline;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull
    @Enumerated(EnumType.STRING)
    private WorkState state;

    @NotNull
    private Integer displayOrder;

    @NotNull
    private Importance importance;

    @Builder.Default
    private Boolean noticed = false;

    @Builder.Default
    private Boolean isDeleted = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void updateState(WorkState state) {
        this.state = state;
    }

    public void updateOrder(Integer order) {
        this.displayOrder = order;
    }

    public void updateNoticed(boolean isNoticed) {
        this.noticed = isNoticed;
    }

    public void updateImportance(Importance importance) {
        this.importance = importance;
    }

    public String toTempNotificationMessage() {

        String date = this.getDate().toString();
        String title = this.getTitle();
        String  deadline = this.getDeadline().format(Constants.DATE_TIME_FORMAT);

        return String.format("%s에 작성된 업무 : %s의 마감기한이 %s까지 ", date, title, deadline);
    }

    @Override
    public String toString() {
        return "Work{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", deadline=" + deadline +
                ", category=" + category +
                ", state=" + state +
                ", displayOrder=" + displayOrder +
                ", importance=" + importance +
                ", noticed=" + noticed +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
