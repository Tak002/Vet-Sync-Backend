package com.vetsync.backend.domain;

import com.vetsync.backend.global.enums.MedicalValueType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Entity
@Table(name = "task_definitions")
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
    private boolean isGlobal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicalValueType valueType;

    private String description;
}
