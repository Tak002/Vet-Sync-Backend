package com.vetsync.backend.dto.hospital;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "병원 등록 요청 DTO")
public record HospitalRegisterRequest(
        @NotEmpty(message = "병원 이름은 필수입니다")
        @Schema(description = "병원 이름", example = "행복동물병원")
        String name
) {
}
