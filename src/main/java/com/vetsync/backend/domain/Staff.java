package com.vetsync.backend.domain;

import com.vetsync.backend.global.BaseTimeEntity;
import com.vetsync.backend.global.enums.StaffRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    @JdbcTypeCode(SqlTypes.NAMED_ENUM) //varchar가 아니라 “DB named enum”으로 바인딩
    @Column(nullable = false, columnDefinition = "staff_role") //컬럼이 그 타입임을 명시
    private StaffRole role;

    @Column(nullable = false)
    private boolean isActive;

    @Builder
    public Staff(Hospital hospital, String loginId, String password, String name, StaffRole role, boolean isActive) {
        this.hospital = hospital;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.role = role;
        this.isActive = isActive;
    }
}
