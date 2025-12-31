package com.vetsync.backend.dto.patientDayNote;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

@Schema(description = "환자 일일 메모 업서트 요청")
public record PatientDayNoteUpsertRequest(

        @Schema(
                description = "줄 번호 -> 내용",
                example = """
                        {"2":"abc","4":"sdf"}
                        """
        )
        @NotNull
        Map<Integer, String> content
) {}
