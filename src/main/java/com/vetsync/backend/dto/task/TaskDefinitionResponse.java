package com.vetsync.backend.dto.task;

import com.vetsync.backend.domain.TaskDefinition;
import com.vetsync.backend.global.enums.MedicalValueType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "TaskDefinitionResponse", description = "업무 정의 응답 DTO")
    public record TaskDefinitionResponse(
            @Schema(example = "11111111-1111-1111-1111-111111111111")
            UUID id,

            @Schema(example = "체중")
            String name,

            @Schema(example = "true")
            boolean global,

            @Schema(description = "병원 전용일 때만 값이 존재", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6", nullable = true)
            UUID hospitalId,

            @Schema(example = "FLOAT")
            MedicalValueType valueType,

            @Schema(example = "환자 체중 측정", nullable = true)
            String description
    ) {
        public static TaskDefinitionResponse from(TaskDefinition e) {
            return new TaskDefinitionResponse(
                    e.getId(),
                    e.getName(),
                    e.isGlobal(),
                    e.getHospital() == null ? null : e.getHospital().getId(),
                    e.getValueType(),
                    e.getDescription()
            );
        }
    }