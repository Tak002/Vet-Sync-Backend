package com.vetsync.backend.dto.patient;

import com.vetsync.backend.global.enums.PatientGender;
import com.vetsync.backend.global.enums.PatientSpecies;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "환자 등록 요청")
public record PatientRegisterRequest(
        @Schema(
                description = "보호자 ID",
                example = "fd2aceff-861f-4a27-886d-133c9d687a17",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull UUID ownerId,

        @Schema(
                description = "환자 이름",
                example = "초코",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String name,

        @Schema(
                description = "종(species)",
                example = "DOG",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        PatientSpecies species,

        @Schema(
                description = "종 상세 (기타 종일 경우)",
                example = "포메라니안"
        )
        String speciesDetail,

        @Schema(
                description = "품종",
                example = "말티즈"
        )
        String breed,

        @Schema(
                description = "성별",
                example = "M",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        PatientGender gender
) {
}
