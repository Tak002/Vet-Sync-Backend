package com.vetsync.backend.dto.task;

import com.vetsync.backend.domain.TaskDefinition;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;
import java.util.UUID;

@Schema(name = "TaskDefinitionResponse", description = "업무 정의 응답 DTO")
    public record TaskDefinitionResponse(
            @Schema(example = "11111111-1111-1111-1111-111111111111")
            UUID id,

            @Schema(example = "체중")
            String name,

            @Schema(example = "true")
            boolean fixed,

            @Schema(example = "환자 체중 측정", nullable = true)
            String description,

            @Schema(description = "옵션 정의(JSON)", example = "{}")
            Map<String, Object> options
    ) {
        public static TaskDefinitionResponse from(TaskDefinition e) {
            return new TaskDefinitionResponse(
                    e.getId(),
                e.getName(),
                    e.isFixed(),
                    e.getDescription(),
                    e.getOptions()
            );
        }
    }