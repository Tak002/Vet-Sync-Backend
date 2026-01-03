package com.vetsync.backend.dto.owner;

import com.vetsync.backend.global.annotation.ValidPhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "보호자 등록 요청")
public record OwnerRegisterRequest(

        @Schema(
                description = "보호자 이름",
                example = "김민수",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank
        @Size(max = 50)
        String name,

        @Schema(
                description = "전화번호 (010으로 시작하는 11자리 숫자)",
                example = "01012345678"
        )
        @ValidPhoneNumber
        String phone,


        @Schema(
                description = "이메일",
                example = "minsu.kim@example.com"
        )
        @Email
        @Size(max = 100)
        String email,

        @Schema(
                description = "주소",
                example = "서울특별시 강남구 테헤란로 123"
        )
        @Size(max = 255)
        String address,

        @Schema(
                description = "메모",
                example = "야간 방문 가능"
        )
        @Size(max = 500)
        String memo
) {}
