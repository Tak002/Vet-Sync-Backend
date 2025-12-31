package com.vetsync.backend.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "로그인 요청 DTO")
public record LoginRequest(

        @Schema(
                description = "병원 ID",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull
        UUID hospitalId,

        @Schema(
                description = "로그인 아이디",
                example = "front01",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String loginId,

        @Schema(
                description = "비밀번호",
                example = "password123!",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        String password
) {}
