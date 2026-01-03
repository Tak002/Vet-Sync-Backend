package com.vetsync.backend.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "업무 생성 요청")
public record TaskCreateRequest(

        @Schema(
                description = "환자 ID",
                example = "e2da4625-7158-496f-9f84-9d26b7086ef2"
        )
        @NotNull UUID patientId,

        @Schema(
                description = "업무 정의 ID (task_definitions.id)",
                example = "11111111-1111-1111-1111-111111111111"
        )
        @NotNull UUID taskDefinitionId,

        @Schema(
                description = "업무 날짜",
                example = "2026-01-01"
        )
        @NotNull LocalDate taskDate,

        @Schema(
                description = "업무 시간(0~23)",
                example = "10",
                minimum = "0",
                maximum = "23"
        )
        @NotNull
        @Min(0) @Max(23)
        Integer taskHour,

        @Schema(
                description = "업무 메모",
                example = "체온 측정 후 기록"
        )
        String taskNotes,

        @Schema(
                description = "담당자 staffId (없으면 null)",
                example = "214c43a5-5a9a-4c75-8f3f-669c10136e5a",
                nullable = true
        )
        UUID assigneeId
) {}
