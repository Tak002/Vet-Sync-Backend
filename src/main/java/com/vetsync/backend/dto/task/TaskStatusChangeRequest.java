package com.vetsync.backend.dto.task;

import com.vetsync.backend.global.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "업무 상태 변경 요청")
public record TaskStatusChangeRequest(
        @Schema(description = "변경할 상태", example = "IN_PROGRESS")
        @NotNull TaskStatus status,

        @Schema(description = "결과(상태 변경 시 함께 저장). COMPLETED에서 필수로 쓰고 싶으면 서비스에서 검증", example = "환자 체온 38.5 확인")
        String result
) {}
