package com.vetsync.backend.dto.feeding;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "환자 일일 급여 업서트 요청")
public record FeedingUpsertRequest(

        @Schema(description = "사료 또는 기본 식단", example = "i/d low fat can loog + 죽토핑")
        @NotBlank @Size(max = 255)
        String diet,

        @Schema(description = "아침 메뉴", example = "닭죽 50g")
        @Size(max = 255)
        String breakfastMenu,

        @Schema(description = "아침 섭취 상태 (1: 절폐, 2: 감소, 3: 정상, 4: 강제급여), 0은 선택 없음", example = "3")
        @Min(0) @Max(4)
        Integer breakfastStatus,

        @Schema(description = "점심 메뉴", example = "사료 30g")
        @Size(max = 255)
        String lunchMenu,

        @Schema(description = "점심 섭취 상태 (0~4)", example = "2")
        @Min(0) @Max(4)
        Integer lunchStatus,

        @Schema(description = "저녁 메뉴", example = "닭죽 50g")
        @Size(max = 255)
        String dinnerMenu,

        @Schema(description = "저녁 섭취 상태 (0~4)", example = "3")
        @Min(0) @Max(4)
        Integer dinnerStatus
) {}
