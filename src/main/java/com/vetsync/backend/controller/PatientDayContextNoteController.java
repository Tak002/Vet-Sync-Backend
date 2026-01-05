package com.vetsync.backend.controller;

import com.vetsync.backend.dto.patientDayNote.PatientDayContextNoteResponse;
import com.vetsync.backend.dto.patientDayNote.PatientDayContextNoteUpsertRequest;
import com.vetsync.backend.global.annotation.HospitalId;
import com.vetsync.backend.service.PatientDayContextNoteService;
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
@RequestMapping("/patients/{patientId}/day-context-notes")
@RequiredArgsConstructor
@Tag(name = "PatientDayContextNote", description = "환자 일일 업무시 확인 사항 등록/수정 API")
public class PatientDayContextNoteController {
    private final PatientDayContextNoteService patientDayContextNoteService;

    /**
     * Retrieve the patient's day-context note for the specified date.
     *
     * @param hospitalId the hospital identifier injected by the {@code @HospitalId} resolver
     * @param patientId  the patient identifier from the request path
     * @param date       the date to retrieve the note for (ISO-8601)
     * @return           the patient's day-context note response
     */
    @GetMapping("/{date}")
    @Operation(summary = "환자 일일 메모 조회", description = "해당 환자의 일일 메모를 조회합니다.")
    public ResponseEntity<PatientDayContextNoteResponse> get(
            @HospitalId UUID hospitalId,
            @PathVariable UUID patientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(patientDayContextNoteService.get(hospitalId, patientId, date));
    }

    /**
     * Save or replace a patient's day context note for the given date.
     *
     * @param hospitalId the identifier of the hospital (injected via {@code @HospitalId})
     * @param patientId  the identifier of the patient
     * @param date       the date of the day context note (ISO format)
     * @param req        the upsert request payload containing the note content
     * @return           the saved PatientDayContextNoteResponse
     */
    @PutMapping("/{date}")
    @Operation(summary = "환자 일일 메모 저장(업서트)", description = "해당 환자의 일일 메모를 통째로 저장합니다. (없으면 생성, 있으면 덮어쓰기)")
    public ResponseEntity<PatientDayContextNoteResponse> upsert(
            @HospitalId UUID hospitalId,
            @PathVariable UUID patientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Valid @RequestBody PatientDayContextNoteUpsertRequest req
    ) {
        return ResponseEntity.ok(patientDayContextNoteService.upsert(hospitalId, patientId, date, req));
    }
}