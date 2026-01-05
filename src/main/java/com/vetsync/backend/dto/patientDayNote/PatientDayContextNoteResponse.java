package com.vetsync.backend.dto.patientDayNote;

import com.vetsync.backend.domain.PatientDayContextNote;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Schema(description = "환자 일일 메모 응답")
public record PatientDayContextNoteResponse(
        @Schema(description = "메모 ID")
        UUID id,

        @Schema(description = "줄 번호 -> 내용")
        Map<Integer, String> content,

        @Schema(description = "수정 시각")
        OffsetDateTime updatedAt
) {
    public static PatientDayContextNoteResponse from(PatientDayContextNote e) {
        return new PatientDayContextNoteResponse(
                e.getId(),
                e.getContent(),
                e.getUpdatedAt()
        );
    }
}
