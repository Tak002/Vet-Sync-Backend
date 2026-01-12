package com.vetsync.backend.dto.patientDayTaskDefinitionNote;

import com.vetsync.backend.domain.PatientDayTaskDefinitionNote;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "환자/일자/TaskDefinition 단위 공용 노트 정보")
public record PatientDayTaskDefinitionNoteInfoResponse(

        @Schema(description = "공용 노트 ID", example = "6fc17002-84c5-4d6b-a031-3f97e84f8495")
        UUID id,

        @Schema(description = "TaskDefinition ID", example = "22222222-2222-2222-2222-222222222221")
        UUID taskDefinitionId,

        @Schema(description = "공용 노트 내용", example = "소진시 내복약 복용")
        String content,

        @Schema(description = "선택 옵션 key 배열", example = "[1,2]")
        Short[] selectedOptionKeys
) {

    public static PatientDayTaskDefinitionNoteInfoResponse from(PatientDayTaskDefinitionNote entity) {
        return new PatientDayTaskDefinitionNoteInfoResponse(
                entity.getId(),
                entity.getTaskDefinition().getId(),
                entity.getContent(),
                entity.getSelectedOptionKeys()
        );
    }
}
