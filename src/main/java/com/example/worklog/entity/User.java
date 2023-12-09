package com.example.worklog.entity;

import com.example.worklog.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;
    private Boolean isDeleted;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<Work> works = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<SavedWork> savedWorks = new ArrayList<>();

    public void updatePassword(String password) {
        this.password = password;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
