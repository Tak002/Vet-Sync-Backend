package com.vetsync.backend.dto.task;

import com.vetsync.backend.domain.Task;
import com.vetsync.backend.global.enums.TaskStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TaskInfoResponse(
        UUID id,
        UUID hospitalId,
        UUID patientId,
        LocalDate taskDate,
        Integer taskHour,
        UUID taskDefinitionId,
        String taskNotes,
        TaskStatus status,
        String result,
        UUID assigneeId,
        OffsetDateTime createdAt,
        UUID createdBy,
        OffsetDateTime startedAt,
        OffsetDateTime confirmRequestedAt,
        OffsetDateTime completedAt,
        OffsetDateTime updatedAt
) {
    public static TaskInfoResponse from(Task e) {
        return new TaskInfoResponse(
                e.getId(),
                e.getHospital().getId(),
                e.getPatient().getId(),
                e.getTaskDate(),
                e.getTaskHour(),
                e.getTaskDefinition().getId(),
                e.getTaskNotes(),
                e.getStatus(),
                e.getResult(),
                e.getAssignee() == null ? null : e.getAssignee().getId(),
                e.getCreatedAt(),
                e.getCreatedBy().getId(),
                e.getStartedAt(),
                e.getConfirmRequestedAt(),
                e.getCompletedAt(),
                e.getUpdatedAt()
        );
    }
}