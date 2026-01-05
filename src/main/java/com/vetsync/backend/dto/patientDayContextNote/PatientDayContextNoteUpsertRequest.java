package com.vetsync.backend.dto.patientDayContextNote;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

@Schema(description = "환자 일일 메모 업서트 요청")
public record PatientDayContextNoteUpsertRequest(

        @Schema(
                description = "줄 번호 -> 내용 (줄 번호: 1~20, 내용: 최대 40자)",
                example = """
                {"2":"abc","4":"sdf"}
                """
        )
        @NotNull
        Map<@Min(1) @Max(20) Integer, @Size(max = 40) String> content
) {}
