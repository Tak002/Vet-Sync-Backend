package com.vetsync.backend.controller;

import com.vetsync.backend.dto.patient.PatientInfoResponse;
import com.vetsync.backend.dto.patient.PatientRegisterRequest;
import com.vetsync.backend.dto.patient.PatientDiagnosisUpdateRequest;
import com.vetsync.backend.global.annotation.HospitalId;
import com.vetsync.backend.global.annotation.StaffId;
import com.vetsync.backend.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Patient", description = "환자 등록/관리 API")
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping
    @Operation(summary = "환자 등록", description = "새로운 환자를 등록합니다")
    public ResponseEntity<?> registerPatient(@HospitalId UUID hospitalId, @StaffId UUID staffId,
                                             @Valid @RequestBody PatientRegisterRequest patientRegisterRequest) {
        PatientInfoResponse patientInfoResponse = patientService.registerPatient(hospitalId, staffId,patientRegisterRequest);
        return ResponseEntity.ok(patientInfoResponse);
    }

    @GetMapping("/{patientId}")
    @Operation(summary = "환자 정보 조회", description = "환자의 정보를 조회합니다")
    public ResponseEntity<PatientInfoResponse> getPatientInfo(@HospitalId UUID hospitalId, @PathVariable UUID patientId) {
        return ResponseEntity.ok(patientService.getPatientInfo(hospitalId, patientId));
    }

    @PatchMapping("/{patientId}")
    @Operation(summary = "환자 진단 정보 수정", description = "환자의 cc/diagnosis를 수정합니다")
    public ResponseEntity<PatientInfoResponse> updatePatient(
            @HospitalId UUID hospitalId,
            @PathVariable UUID patientId,
            @Valid @RequestBody PatientDiagnosisUpdateRequest request
    ) {
        return ResponseEntity.ok(patientService.updateDiagnosis(hospitalId, patientId, request));
    }
}
