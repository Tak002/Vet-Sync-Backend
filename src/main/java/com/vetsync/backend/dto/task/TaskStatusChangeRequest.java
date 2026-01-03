package com.vetsync.backend.dto.task;

import com.vetsync.backend.global.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "업무 상태 변경 요청 (상태 + 결과)")
public record TaskStatusChangeRequest(

        @Schema(
                description = "변경할 상태 (순서 권장: PENDING -> IN_PROGRESS -> CONFIRM_WAITING -> COMPLETED)",
                example = "IN_PROGRESS"
        )
        @NotNull TaskStatus status,

        @Schema(
                description = "결과/처리내용 ",
                example = "체온 38.6 확인, 특이사항 없음"
        )
        String result
) {}
