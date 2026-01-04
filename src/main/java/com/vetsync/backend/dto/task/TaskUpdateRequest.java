package com.vetsync.backend.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.UUID;

@Schema(description = "Task 일반 수정 요청 (status 제외)")
public record TaskUpdateRequest(

        @Schema(description = "업무 메모", example = "체온 측정 후 2시간 뒤 재측정")
        String taskNotes,

        @Schema(description = "업무 결과/기록", example = "체온 38.9, 식욕 저하 관찰됨")
        String result,

        @Schema(description = "담당자(Staff) ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
        // "" 와 null을 구분하기 위해서 String 사용
        // null 이면 변경 없음, "" 이면 담당자 미지정
        @UUID(allowEmpty = true)
        String assigneeId
) {}
