package com.vetsync.backend.controller;

import com.vetsync.backend.dto.patientDayTaskDefinitionNote.PatientDayTaskDefinitionNoteCreateRequest;
import com.vetsync.backend.dto.patientDayTaskDefinitionNote.PatientDayTaskDefinitionNoteInfoResponse;
import com.vetsync.backend.dto.patientDayTaskDefinitionNote.PatientDayTaskDefinitionNoteUpdateRequest;
import com.vetsync.backend.global.annotation.HospitalId;
import com.vetsync.backend.service.PatientDayTaskDefinitionNoteService;
import com.vetsync.backend.service.TaskAndDefinitionNoteCreationFacade;
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

@RestController
@RequestMapping("/tasks/patients/{patientId}/{taskDate}/definition-notes")
@RequiredArgsConstructor
@Tag(name = "Task Definition Notes", description = "Task 수행 시 사용하는 TaskDefinition 단위 공용 노트")
public class PatientDayTaskDefinitionNoteController {

    private final PatientDayTaskDefinitionNoteService patientDayTaskDefinitionNoteService;
    private final TaskAndDefinitionNoteCreationFacade taskAndDefinitionNoteCreationFacade;

    // =========================
    // GET: 목록 조회
    // =========================
    @Operation(
            summary = "TaskDefinition 공용 노트 목록 조회",
            description = "특정 환자의 특정 날짜에 대한 TaskDefinition 단위 공용 노트 목록을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<PatientDayTaskDefinitionNoteInfoResponse>> getDefinitionNotes(
            @HospitalId UUID hospitalId,
            @PathVariable UUID patientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate taskDate
    ) {
        return ResponseEntity.ok(
                patientDayTaskDefinitionNoteService.getDefinitionNotes(hospitalId, patientId, taskDate)
        );
    }

    // =========================
    // POST: 생성
    // =========================
    @Operation(
            summary = "TaskDefinition 공용 노트 생성",
            description = "특정 TaskDefinition에 대한 공용 노트를 생성하고, 관련 task들과 자동으로 연결합니다." +
                    "기존 메모가 공백인 경우에도 동작합니다"
    )
    @PostMapping
    public ResponseEntity<PatientDayTaskDefinitionNoteInfoResponse> createDefinitionNote(
            @HospitalId UUID hospitalId,
            @PathVariable UUID patientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate taskDate,
            @Valid @RequestBody PatientDayTaskDefinitionNoteCreateRequest request
    ) {
        return ResponseEntity.ok(
                taskAndDefinitionNoteCreationFacade.createPatientDayTaskDefinition(
                        hospitalId, patientId, taskDate, request
                )
        );
    }

    // =========================
    // PATCH: 단건 수정
    // =========================
    @Operation(
            summary = "TaskDefinition 공용 노트 단건 수정",
            description = "공용 노트 내용을 수정합니다. note가 null이면 변경하지 않고, 빈 문자열이면 비웁니다."
    )
    @PatchMapping("/{noteId}")
    public ResponseEntity<PatientDayTaskDefinitionNoteInfoResponse> updateDefinitionNote(
            @HospitalId UUID hospitalId,
            @PathVariable UUID patientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate taskDate,
            @PathVariable UUID noteId,
            @Valid @RequestBody PatientDayTaskDefinitionNoteUpdateRequest request
    ) {
        return ResponseEntity.ok(
                patientDayTaskDefinitionNoteService.updateDefinitionNote(
                        hospitalId, patientId, taskDate, noteId, request
                )
        );
    }
}
