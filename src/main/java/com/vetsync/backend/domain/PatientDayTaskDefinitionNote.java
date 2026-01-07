package com.vetsync.backend.domain;

import com.vetsync.backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
        name = "patient_day_task_definition_notes",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_pdtddn_unique",
                columnNames = {"hospital_id", "patient_id", "task_date", "task_definition_id"}
        )
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"hospital", "patient", "taskDefinition"})
public class PatientDayTaskDefinitionNote extends BaseTimeEntity {

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

    @Column(name = "task_date", nullable = false)
    private LocalDate taskDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_definition_id", nullable = false)
    private TaskDefinition taskDefinition;

    @Column(name = "content", nullable = false)
    @Builder.Default
    private String content = "";

    // 다중 선택 키 저장: smallint[]
    @Column(name = "selected_option_keys", nullable = false, columnDefinition = "smallint[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Builder.Default
    private Short[] selectedOptionKeys = new Short[]{};
}
