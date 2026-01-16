package com.vetsync.backend.dto.feeding;

import com.vetsync.backend.domain.Feeding;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.UUID;

@Schema(description = "환자 일일 식단 응답")
public record FeedingResponse(
        @Schema(description = "식단 ID")
        UUID id,

        @Schema(description = "사료 또는 기본 식단")
        String diet,

        @Schema(description = "아침 메뉴")
        String breakfastMenu,
        @Schema(description = "아침 섭취 상태 (1~4)")
        short breakfastStatus,

        @Schema(description = "점심 메뉴")
        String lunchMenu,
        @Schema(description = "점심 섭취 상태 (1~4)")
        short lunchStatus,

        @Schema(description = "저녁 메뉴")
        String dinnerMenu,
        @Schema(description = "저녁 섭취 상태 (1~4)")
        short dinnerStatus,

        @Schema(description = "수정 시각")
        OffsetDateTime updatedAt
) {
    public static FeedingResponse from(Feeding f) {
        return new FeedingResponse(
                f.getId(),
                f.getDiet(),
                f.getBreakfastMenu(),
                f.getBreakfastStatus(),
                f.getLunchMenu(),
                f.getLunchStatus(),
                f.getDinnerMenu(),
                f.getDinnerStatus(),
                f.getUpdatedAt()
        );
    }

    public static FeedingResponse empty() {
        return new FeedingResponse(
                null,
                "",
                "",
                (short)3,
                "",
                (short)3,
                "",
                (short)3,
                null
        );
    }
}
