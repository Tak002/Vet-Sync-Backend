package com.vetsync.backend.domain;

import com.vetsync.backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(
        name = "patient_day_context_notes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_patient_day_context_notes_unique",
                        columnNames = {"hospital_id", "patient_id", "note_date"}
                )
        },
        indexes = {
                @Index(
                        name = "ix_pdcn_day_lookup",
                        columnList = "hospital_id, patient_id, note_date"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 전용, 개발자 접근 차단
@AllArgsConstructor
@Builder
@ToString(exclude = {"hospital", "patient"})
public class PatientDayContextNote extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // 필수 연관관계
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) // 필수 연관관계
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "note_date", nullable = false)
    private LocalDate noteDate;

    /**
     * jsonb 컬럼.
     * 예: {"1":"1번줄내용","2":"2번줄내용"}
     *
     * - 키: lineNo (문자열로 저장: "1","2"...)
     * - 값: 해당 줄 내용
     *
     * LinkedHashMap을 쓰면 (Jackson 직렬화/역직렬화 시) 삽입 순서를 최대한 유지하기 좋음.
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    @Builder.Default
    private Map<Integer, String> content = new HashMap<>();
}
