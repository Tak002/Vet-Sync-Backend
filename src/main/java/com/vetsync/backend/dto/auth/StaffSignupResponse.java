package com.vetsync.backend.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "직원 회원가입 응답")
public record StaffSignupResponse(
    @Schema(description = "직원 ID")
    UUID staffId
) {}
