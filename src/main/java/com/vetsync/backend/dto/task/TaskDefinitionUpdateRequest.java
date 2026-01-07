package com.vetsync.backend.dto.task;

import com.vetsync.backend.global.annotation.ValidOptionMap;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
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
        Map<String, String> options,

        @Schema(description = "고정 항목의 표시 순서(1..N). fixed=false인 경우 무시됨", example = "2")
        @Min(1)
        Integer order
) {}
