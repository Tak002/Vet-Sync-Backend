package com.vetsync.backend.dto.patientDayTaskDefinitionNote;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "TaskDefinition 공용 노트 수정 요청")
public record PatientDayTaskDefinitionNoteUpdateRequest(

        @Schema(
                description = "공용 노트 내용 (null이면 변경 없음, \"\"이면 비우기)",
                example = "식욕 저하 지속 관찰"
        )
        String note
) {
}
