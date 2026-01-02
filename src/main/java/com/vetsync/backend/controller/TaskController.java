package com.vetsync.backend.controller;

import com.vetsync.backend.dto.task.TaskCreateRequest;
import com.vetsync.backend.dto.task.TaskInfoResponse;
import com.vetsync.backend.dto.task.TaskStatusChangeRequest;
import com.vetsync.backend.global.annotation.HospitalId;
import com.vetsync.backend.global.annotation.StaffId;
import com.vetsync.backend.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "Task", description = "업무 등록/관리 API")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(
            summary = "업무 등록",
            description = """
                    업무를 생성합니다.
                    - hospitalId, staffId는 JWT에서 주입됩니다.
                    - 예시 patientId: e2da4625-7158-496f-9f84-9d26b7086ef2 (초코)
                    - 예시 staffId(createdBy): 214c43a5-5a9a-4c75-8f3f-669c10136e5a (홍길동)
                    """
    )
    public ResponseEntity<TaskInfoResponse> create(
            @Parameter(hidden = true) @HospitalId UUID hospitalId,
            @Parameter(hidden = true) @StaffId UUID staffId,
            @Valid @RequestBody TaskCreateRequest req
    ) {
        return ResponseEntity.ok(taskService.create(hospitalId, staffId, req));
    }

    @PatchMapping("/{taskId}/status")
    @Operation(
            summary = "업무 상태 변경",
            description = """
                    업무 상태를 변경합니다. (요청에 result 포함)
                    - 상태는 반드시 PENDING -> IN_PROGRESS -> CONFIRM_WAITING -> COMPLETED 순서로만 변경되도록 서비스에서 검증해야 합니다.
                    """
    )
    public ResponseEntity<TaskInfoResponse> changeStatus(
            @Parameter(hidden = true) @HospitalId UUID hospitalId,
            @Parameter(
                    description = "업무 ID",
                    example = "b9f5f9a1-3a3e-4a54-9cb2-41f04b9a2d11"
            )
            @PathVariable UUID taskId,
            @Valid @RequestBody TaskStatusChangeRequest req
    ) {
        return ResponseEntity.ok(taskService.changeStatus(hospitalId, taskId, req));
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "업무 단건 조회", description = "업무를 단건 조회합니다.")
    public ResponseEntity<TaskInfoResponse> getSingleTask(
            @Parameter(hidden = true) @HospitalId UUID hospitalId,
            @Parameter(
                    description = "업무 ID",
                    example = "b9f5f9a1-3a3e-4a54-9cb2-41f04b9a2d11"
            )
            @PathVariable UUID taskId
    ) {
        return ResponseEntity.ok(taskService.getSingleTask(hospitalId, taskId));
    }

    @GetMapping("/patients/{patientId}")
    @Operation(summary = "환자 업무 전체 조회", description = "해당 환자의 모든 업무를 조회합니다.")
    public ResponseEntity<List<TaskInfoResponse>> getPatientTasks(
            @Parameter(hidden = true) @HospitalId UUID hospitalId,
            @Parameter(
                    description = "환자 ID",
                    example = "e2da4625-7158-496f-9f84-9d26b7086ef2"
            )
            @PathVariable UUID patientId
    ) {
        return ResponseEntity.ok(taskService.getPatientTasks(hospitalId, patientId));
    }

    @GetMapping("/patients/{patientId}/days/{taskDate}")
    @Operation(summary = "환자 특정 날짜 업무 조회", description = "해당 환자의 특정 날짜(taskDate)의 업무를 조회합니다.")
    public ResponseEntity<List<TaskInfoResponse>> getPatientDayTasks(
            @Parameter(hidden = true) @HospitalId UUID hospitalId,
            @Parameter(
                    description = "환자 ID",
                    example = "e2da4625-7158-496f-9f84-9d26b7086ef2"
            )
            @PathVariable UUID patientId,
            @Parameter(
                    description = "업무 날짜 (yyyy-MM-dd)",
                    example = "2026-01-01"
            )
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate taskDate
    ) {
        return ResponseEntity.ok(taskService.getPatientDayTasks(hospitalId, patientId, taskDate));
    }
}
