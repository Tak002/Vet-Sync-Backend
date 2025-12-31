package com.vetsync.backend.contorller;

import com.vetsync.backend.dto.patientDayNote.PatientDayNoteResponse;
import com.vetsync.backend.dto.patientDayNote.PatientDayNoteUpsertRequest;
import com.vetsync.backend.global.annotation.HospitalId;
import com.vetsync.backend.service.PatientDayNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/patients/{patientId}/day-notes")
@RequiredArgsConstructor
@Tag(name = "PatientDayNote", description = "환자 일일 업무시 확인 사항 등록/수정 API")
public class PatientDayNoteController {
    private final PatientDayNoteService patientDayNoteService;

    @GetMapping("/{date}")
    @Operation(summary = "환자 일일 메모 조회", description = "해당 환자의 일일 메모를 조회합니다.")
    public ResponseEntity<PatientDayNoteResponse> get(
            @HospitalId UUID hospitalId,
            @PathVariable UUID patientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(patientDayNoteService.get(hospitalId, patientId, date));
    }

    @PutMapping("/{date}")
    @Operation(summary = "환자 일일 메모 저장(업서트)", description = "해당 환자의 일일 메모를 통째로 저장합니다. (없으면 생성, 있으면 덮어쓰기)")
    public ResponseEntity<PatientDayNoteResponse> upsert(
            @HospitalId UUID hospitalId,
            @PathVariable UUID patientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Valid @RequestBody PatientDayNoteUpsertRequest req
    ) {
        return ResponseEntity.ok(patientDayNoteService.upsert(hospitalId, patientId, date, req));
    }
}
