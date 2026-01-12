package com.vetsync.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(
        name = "task_definitions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_task_definition_hospital_name", columnNames = {"hospital_id", "name"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"hospital"})
public class TaskDefinition {

    @Id
    @Column(columnDefinition = "uuid")
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private boolean isFixed;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    private String description;

    // fixed 항목의 정렬 순서 (1..N). 비고정(false)인 경우 null
    @Column(name = "order_no")
    private Integer order;

    // JSONB options: e.g., {"1":"위액","2":"음식물"}
    @Column(columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> options = new HashMap<>();
    
}
