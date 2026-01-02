package com.vetsync.backend.domain;

import com.vetsync.backend.global.BaseTimeEntity;
import com.vetsync.backend.global.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"hospital", "patient", "taskDefinition", "assignee", "createdBy"})
public class Task extends BaseTimeEntity {

    @Id
    @Column(columnDefinition = "uuid")
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // 업무 예정 날짜 (YYYY-MM-DD)
    @Column(name = "task_date", nullable = false)
    private LocalDate taskDate;

    // 업무 예정 시간 (0~23)
    @Column(name = "task_hour", nullable = false)
    private Integer taskHour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_definition_id", nullable = false)
    private TaskDefinition taskDefinition;

    private String taskNotes;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    @Builder.Default
    private TaskStatus status= TaskStatus.PENDING;

    private String result;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private Staff assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Staff createdBy;

    // Timestamps for status changes
    private OffsetDateTime startedAt; // when the status changed to IN_PROGRESS
    private OffsetDateTime confirmRequestedAt; // when the status changed to CONFIRM_WAITING
    private OffsetDateTime completedAt; // when the status changed to COMPLETED
}
