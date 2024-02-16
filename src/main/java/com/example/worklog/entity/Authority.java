package com.example.worklog.entity;

import com.example.worklog.entity.enums.AuthorityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private AuthorityType authorityType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
