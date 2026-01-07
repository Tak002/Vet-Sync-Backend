package com.vetsync.backend.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import com.vetsync.backend.global.annotation.ValidOptionMap;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

@Schema(name = "TaskDefinitionUpdateRequest", description = "업무 정의 수정 요청 DTO")
public record TaskDefinitionUpdateRequest(
        @NotBlank
        @Schema(example = "체중")
        String name,

        @NotNull
        @Schema(example = "true")
        Boolean fixed,

        @Schema(example = "환자 체중 측정", nullable = true)
        String description,

        @NotNull
        @ValidOptionMap
        @Schema(description = "옵션 정의(JSON)", example = "{\"1\":\"위액\",\"2\":\"음식물\",\"3\":\"혈액\",\"4\":\"거품\",\"5\":\"시간당2회이상\"}")
        Map<String, String> options
) {}
