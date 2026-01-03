package com.vetsync.backend.dto.auth;

import com.vetsync.backend.global.enums.StaffRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "직원 회원가입 요청")
public record StaffSignupRequest(

    @Schema(description = "병원 ID")
    @NotNull
    UUID hospitalId,

    @Schema(description = "로그인 ID", example = "front01")
    @NotBlank
    String loginId,

    @Schema(description = "비밀번호", example = "password123!")
    @NotBlank
    String password,

    @Schema(description = "직원 이름", example = "홍길동")
    @NotBlank
    String name,

    @Schema(description = "직원 역할")
    @NotNull
    StaffRole role
) {}
