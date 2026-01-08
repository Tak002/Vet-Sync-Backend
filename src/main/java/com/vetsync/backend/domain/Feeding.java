package com.vetsync.backend.domain;

import com.vetsync.backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "feedings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_feedings_unique",
                        columnNames = {"hospital_id", "patient_id", "feeding_date"}
                )
        },
        indexes = {
                @Index(name = "ix_feedings_day_lookup", columnList = "hospital_id, patient_id, feeding_date")
        }
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@ToString(exclude = {"hospital", "patient"})
public class Feeding extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "feeding_date", nullable = false)
    private LocalDate feedingDate;

    // 사료(기본 식단)
    @Column(name = "diet", nullable = false)
    private String diet;

    // 아침
    @Column(name = "breakfast_menu", nullable = false)
    private String breakfastMenu;

    @Column(name = "breakfast_status", nullable = false)
    private short breakfastStatus; // 1~4

    // 점심
    @Column(name = "lunch_menu", nullable = false)
    private String lunchMenu;

    @Column(name = "lunch_status", nullable = false)
    private short lunchStatus; // 1~4

    // 저녁
    @Column(name = "dinner_menu", nullable = false)
    private String dinnerMenu;

    @Column(name = "dinner_status", nullable = false)
    private short dinnerStatus; // 1~4
}
