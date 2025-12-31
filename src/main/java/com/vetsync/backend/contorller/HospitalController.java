package com.vetsync.backend.contorller;

import com.vetsync.backend.dto.hospital.HospitalInfoResponse;
import com.vetsync.backend.dto.hospital.HospitalRegisterRequest;
import com.vetsync.backend.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Hospital", description = "동물 병원 등록/관리 API")
@RestController
@RequestMapping("/hospitals")
@RequiredArgsConstructor
public class HospitalController {
    private final HospitalService hospitalService;

    @PostMapping
    @Operation(summary = "동물 병원 등록", description = "새로운 동물 병원을 등록합니다")
    public ResponseEntity<HospitalInfoResponse> registerHospital(@Valid @RequestBody HospitalRegisterRequest hospitalRegisterRequest) {
        HospitalInfoResponse hospitalInfoResponse = hospitalService.registerHospital(hospitalRegisterRequest);
        return ResponseEntity.ok(hospitalInfoResponse);
    }
}
