package com.vetsync.backend.domain;

import com.vetsync.backend.global.BaseTimeEntity;
import com.vetsync.backend.global.enums.StaffRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
    name = "staffs",
    uniqueConstraints = {
        @UniqueConstraint(
                name = "uq_staff_login_per_hospital",
                columnNames = {"hospital_id", "login_id"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class Staff extends BaseTimeEntity {

    @Id
    @Column(columnDefinition = "uuid")
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StaffRole role;

    @Column(nullable = false)
    private boolean isActive = true;
}
