package com.vetsync.backend.contorller;

import com.vetsync.backend.dto.task.TaskCreateRequest;
import com.vetsync.backend.dto.task.TaskInfoResponse;
import com.vetsync.backend.dto.task.TaskStatusChangeRequest;
import com.vetsync.backend.global.annotation.HospitalId;
import com.vetsync.backend.global.annotation.StaffId;
import com.vetsync.backend.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "업무 등록", description = "업무를 생성합니다.")
    public ResponseEntity<TaskInfoResponse> create(
            @HospitalId UUID hospitalId,
            @StaffId UUID staffId,
            @Valid @RequestBody TaskCreateRequest req
    ) {
        return ResponseEntity.ok(taskService.create(hospitalId, staffId, req));
    }

    @PatchMapping("/{taskId}/status")
    @Operation(summary = "업무 상태 변경", description = "업무 상태를 변경합니다. (요청에 result 포함)")
    public ResponseEntity<TaskInfoResponse> changeStatus(
            @HospitalId UUID hospitalId,
            @PathVariable UUID taskId,
            @Valid @RequestBody TaskStatusChangeRequest req
    ) {
        return ResponseEntity.ok(taskService.changeStatus(hospitalId, taskId, req));
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "업무 단건 조회", description = "업무를 단건 조회합니다.")
    public ResponseEntity<TaskInfoResponse> getSingleTask(
            @HospitalId UUID hospitalId,
            @PathVariable UUID taskId
    ) {
        return ResponseEntity.ok(taskService.getSingleTask(hospitalId, taskId));
    }

    @GetMapping("/patients/{patientId}")
    @Operation(summary = "환자 업무 전체 조회", description = "해당 환자의 모든 업무를 조회합니다.")
    public ResponseEntity<List<TaskInfoResponse>> getPatientTasks(
            @HospitalId UUID hospitalId,
            @PathVariable UUID patientId
    ) {
        return ResponseEntity.ok(taskService.getPatientTasks(hospitalId, patientId));
    }

    @GetMapping("/patients/{patientId}/days/{taskDate}")
    @Operation(summary = "환자 특정 날짜 업무 조회", description = "해당 환자의 특정 날짜(taskDate)의 업무를 조회합니다.")
    public ResponseEntity<List<TaskInfoResponse>> getPatientDayTasks(
            @HospitalId UUID hospitalId,
            @PathVariable UUID patientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate taskDate
    ) {
        return ResponseEntity.ok(taskService.getPatientDayTasks(hospitalId, patientId, taskDate));
    }
}
