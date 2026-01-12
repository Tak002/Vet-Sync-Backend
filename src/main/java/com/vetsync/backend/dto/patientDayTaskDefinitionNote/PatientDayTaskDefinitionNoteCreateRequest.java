package com.vetsync.backend.dto.patientDayTaskDefinitionNote;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "TaskDefinition 공용 노트 생성 요청")
public record PatientDayTaskDefinitionNoteCreateRequest(

        @Schema(description = "TaskDefinition ID", example = "22222222-2222-2222-2222-222222222221")
        @NotNull
        UUID taskDefinitionId,

        @Schema(description = "공용 노트 내용", example = "체온 측정 후 2시간 뒤 재측정")
        @NotNull
        String content,

        @Schema(description = "선택 옵션 key 배열 (없으면 빈 배열)", example = "[1,2]")
        Short[] selectedOptionKeys
) {
}
