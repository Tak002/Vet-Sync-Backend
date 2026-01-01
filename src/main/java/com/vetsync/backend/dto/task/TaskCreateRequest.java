package com.vetsync.backend.dto.task;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record TaskCreateRequest(
        @NotNull UUID patientId,
        @NotNull LocalDate taskDate,
        @NotNull Integer taskHour,
        @NotNull UUID taskDefinitionId,
        String taskNotes,
        UUID assigneeId
) {}