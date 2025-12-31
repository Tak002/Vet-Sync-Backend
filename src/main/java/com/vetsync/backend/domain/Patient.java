package com.vetsync.backend.domain;

import com.vetsync.backend.global.enums.PatientGender;
import com.vetsync.backend.global.enums.PatientSpecies;
import com.vetsync.backend.global.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "patients")
@Getter @Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Patient {

    @Id
    @Column(columnDefinition = "uuid")
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PatientSpecies species;

    private String speciesDetail;
    private String breed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PatientGender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PatientStatus status = PatientStatus.REGISTERED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Staff createdBy;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;
}
