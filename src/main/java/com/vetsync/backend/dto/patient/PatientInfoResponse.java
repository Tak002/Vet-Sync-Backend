package com.vetsync.backend.dto.patient;

import com.vetsync.backend.domain.Patient;
import com.vetsync.backend.global.enums.PatientGender;
import com.vetsync.backend.global.enums.PatientSpecies;
import com.vetsync.backend.global.enums.PatientStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record PatientInfoResponse(
        @Schema(description = "환자 ID")
        UUID id,

        @Schema(description = "병원 ID")
        UUID hospitalId,

        @Schema(description = "보호자 ID")
        UUID ownerId,

        @Schema(description = "환자 이름")
        String name,

        @Schema(description = "종(species)")
        PatientSpecies species,

        @Schema(description = "종 상세")
        String speciesDetail,

        @Schema(description = "품종")
        String breed,

        @Schema(description = "성별")
        PatientGender gender,

        @Schema(description = "환자 상태")
        PatientStatus status,

        @Schema(description = "생성자 ID")
        UUID createdBy
) {
    public static PatientInfoResponse from (Patient patient) {
        return new PatientInfoResponse(patient.getId(),
                patient.getHospital().getId(),
                patient.getOwner().getId(),
                patient.getName(),
                patient.getSpecies(),
                patient.getSpeciesDetail(),
                patient.getBreed(),
                patient.getGender(),
                patient.getStatus(),
                patient.getCreatedBy().getId()
        );
    }
}
