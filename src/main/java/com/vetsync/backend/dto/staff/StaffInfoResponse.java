package com.vetsync.backend.dto.staff;


import com.vetsync.backend.domain.Staff;
import com.vetsync.backend.global.enums.StaffRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "직원 정보 응답 (LoginId, Password 제외)")
public record StaffInfoResponse(

        @Schema(description = "직원 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        UUID id,

        @Schema(description = "직원 이름", example = "홍길동")
        String name,

        @Schema(description = "직원 권한/역할", example = "CHIEF_VET")
        StaffRole role,

        @Schema(description = "활성 여부", example = "true")
        boolean isActive
) {
    public static StaffInfoResponse from(Staff staff) {
        return new StaffInfoResponse(
                staff.getId(),
                staff.getName(),
                staff.getRole(),
                staff.isActive()
        );
    }
}