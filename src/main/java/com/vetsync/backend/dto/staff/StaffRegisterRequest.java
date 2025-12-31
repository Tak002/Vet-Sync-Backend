package com.vetsync.backend.dto.staff;

import com.vetsync.backend.global.enums.StaffRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "직원 등록 요청 DTO")
public record StaffRegisterRequest(
    @Schema(description = "직원 이름", example = "홍길동")
    String name,
    @Schema(description = "직원 역할", example = "CHIEF_VET")
    StaffRole role
) {
}
