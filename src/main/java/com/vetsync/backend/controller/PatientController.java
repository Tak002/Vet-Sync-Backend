package com.vetsync.backend.controller;

import com.vetsync.backend.dto.patient.PatientInfoResponse;
import com.vetsync.backend.dto.patient.PatientRegisterRequest;
import com.vetsync.backend.dto.patient.PatientDiagnosisUpdateRequest;
import com.vetsync.backend.global.enums.PatientGender;
import com.vetsync.backend.global.enums.PatientSpecies;
import com.vetsync.backend.global.enums.PatientStatus;
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
import java.util.List;

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

    @GetMapping
    @Operation(summary = "병원 환자 전체 조회", description = "병원의 전체 환자를 조회합니다. 선택적으로 필터를 적용할 수 있습니다.")
    public ResponseEntity<List<PatientInfoResponse>> listPatients(
            @HospitalId UUID hospitalId,
            @RequestParam(required = false) PatientStatus status,
            @RequestParam(required = false) PatientSpecies species,
            @RequestParam(required = false) PatientGender gender,
            @RequestParam(required = false) UUID ownerId,
            @RequestParam(required = false, name = "name") String nameKeyword
    ) {
        return ResponseEntity.ok(
                patientService.listPatients(hospitalId, status, species, gender, ownerId, nameKeyword)
        );
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
