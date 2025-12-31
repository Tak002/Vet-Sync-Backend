package com.vetsync.backend.global.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "직원 역할")
public enum StaffRole {

    @Schema(description = "원장")
    CHIEF_VET,

    @Schema(description = "수의사")
    VET,

    @Schema(description = "수석 테크니션")
    SENIOR_TECH,

    @Schema(description = "테크니션")
    TECH,

    @Schema(description = "프론트")
    FRONT
}