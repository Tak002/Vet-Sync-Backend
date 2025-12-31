package com.vetsync.backend.dto.owner;

import com.vetsync.backend.domain.Owner;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "보호자 정보 응답")
public record OwnerInfoResponse(

        @Schema(
                description = "보호자 ID",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID id,

        @Schema(
                description = "병원 ID",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID hospitalId,

        @Schema(
                description = "보호자 이름",
                example = "김민수"
        )
        String name,

        @Schema(
                description = "전화번호 (010으로 시작하는 11자리 숫자)",
                example = "01012345678",
                nullable = true
        )
        String phone,

        @Schema(
                description = "이메일",
                example = "minsu.kim@example.com",
                nullable = true
        )
        String email,

        @Schema(
                description = "주소",
                example = "서울특별시 강남구 테헤란로 123",
                nullable = true
        )
        String address,

        @Schema(
                description = "메모",
                example = "야간 방문 가능",
                nullable = true
        )
        String memo,

        @Schema(
                description = "활성 여부",
                example = "true"
        )
        boolean isActive,

        @Schema(
                description = "등록자(직원) ID",
                example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
        )
        UUID createdById
) {
    public static OwnerInfoResponse from(Owner owner) {
        // LAZY 엔티티 접근( getHospital().getId() / getCreatedBy().getId() )은
        // 트랜잭션 안이면 안전. (서비스에서 save 직후면 보통 OK)
        return new OwnerInfoResponse(
                owner.getId(),
                owner.getHospital() != null ? owner.getHospital().getId() : null,
                owner.getName(),
                owner.getPhone(),
                owner.getEmail(),
                owner.getAddress(),
                owner.getMemo(),
                owner.isActive(),
                owner.getCreatedBy() != null ? owner.getCreatedBy().getId() : null
        );
    }
}
