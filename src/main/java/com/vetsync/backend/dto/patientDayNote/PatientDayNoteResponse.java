package com.vetsync.backend.dto.patientDayNote;

import com.vetsync.backend.domain.PatientDayNote;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Schema(description = "환자 일일 메모 응답")
public record PatientDayNoteResponse(
        @Schema(description = "메모 ID")
        UUID id,

        @Schema(description = "병원 ID")
        UUID hospitalId,

        @Schema(description = "환자 ID")
        UUID patientId,

        @Schema(description = "메모 날짜(년-월-일)")
        LocalDate date,

        @Schema(description = "줄 번호 -> 내용")
        Map<Integer, String> content,

        @Schema(description = "생성 시각")
        OffsetDateTime createdAt,

        @Schema(description = "수정 시각")
        OffsetDateTime updatedAt
) {
    public static PatientDayNoteResponse from(PatientDayNote e) {
        return new PatientDayNoteResponse(
                e.getId(),
                e.getHospital().getId(),
                e.getPatient().getId(),
                e.getNoteDate(),
                e.getContent(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }
}
