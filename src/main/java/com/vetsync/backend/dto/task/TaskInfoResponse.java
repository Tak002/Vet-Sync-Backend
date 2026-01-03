package com.vetsync.backend.dto.task;

import com.vetsync.backend.domain.Task;
import com.vetsync.backend.global.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "업무 응답")
public record TaskInfoResponse(

        @Schema(example = "b9f5f9a1-3a3e-4a54-9cb2-41f04b9a2d11")
        UUID id,

        @Schema(example = "e2da4625-7158-496f-9f84-9d26b7086ef2")
        UUID patientId,

        @Schema(example = "11111111-1111-1111-1111-111111111111")
        UUID taskDefinitionId,

        @Schema(example = "2026-01-01")
        LocalDate taskDate,

        @Schema(example = "10")
        Integer taskHour,

        @Schema(example = "PENDING")
        TaskStatus status,

        @Schema(example = "체온 38.6 확인, 특이사항 없음")
        String result,

        @Schema(example = "체온 측정 후 기록")
        String taskNotes,

        @Schema(example = "214c43a5-5a9a-4c75-8f3f-669c10136e5a", nullable = true)
        UUID assigneeId,

        @Schema(example = "214c43a5-5a9a-4c75-8f3f-669c10136e5a")
        UUID createdBy,

        @Schema(example = "2026-01-01T06:56:20.387760+00:00")
        OffsetDateTime createdAt,

        @Schema(example = "2026-01-01T06:56:20.387760+00:00")
        OffsetDateTime updatedAt,

        @Schema(example = "2026-01-01T06:56:20.387760+00:00")
        OffsetDateTime startedAt,

        @Schema(example = "2026-01-01T06:56:20.387760+00:00")
        OffsetDateTime confirmRequestedAt,

        @Schema(example = "2026-01-01T06:56:20.387760+00:00")
        OffsetDateTime completedAt
) {
    public static TaskInfoResponse from(Task e) {
        return new TaskInfoResponse(
                e.getId(),
                e.getPatient().getId(),
                e.getTaskDefinition().getId(),
                e.getTaskDate(),
                e.getTaskHour(),
                e.getStatus(),
                e.getResult(),
                e.getTaskNotes(),
                e.getAssignee() == null ? null : e.getAssignee().getId(),
                e.getCreatedBy().getId(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getStartedAt(),
                e.getConfirmRequestedAt(),
                e.getCompletedAt()
        );
    }
}
