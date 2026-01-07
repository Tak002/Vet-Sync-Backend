package com.vetsync.backend.dto.patient;

import io.swagger.v3.oas.annotations.media.Schema;

public record PatientDiagnosisUpdateRequest(
        @Schema(description = "주호소(Chief complaint)")
        String cc,

        @Schema(description = "진단")
        String diagnosis
) {}
