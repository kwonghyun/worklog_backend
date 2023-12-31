package com.example.worklog.entity;

import com.example.worklog.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@SQLDelete(sql = "UPDATE user SET is_deleted = TRUE WHERE id = ?")
@Where(clause = "is_deleted = FALSE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    @Builder.Default
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Work> works = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<SavedWork> savedWorks = new ArrayList<>();

    public void updatePassword(String password) {
        this.password = password;
    }
}
