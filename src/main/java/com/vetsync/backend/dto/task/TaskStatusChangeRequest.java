package com.vetsync.backend.dto.task;

import com.vetsync.backend.global.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "업무 상태 변경 요청")
public record TaskStatusChangeRequest(

        @Schema(
                description = "변경할 상태 (순서 권장: PENDING -> IN_PROGRESS -> CONFIRM_WAITING -> COMPLETED)",
                example = "IN_PROGRESS"
        )
        @NotNull TaskStatus status
) {}
