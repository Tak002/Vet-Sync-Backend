package com.vetsync.backend.controller;

import com.vetsync.backend.dto.staff.StaffInfoResponse;
import com.vetsync.backend.global.annotation.HospitalId;
import com.vetsync.backend.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Staff", description = "직원 조회/관리 API")
@RestController
@RequestMapping("/staffs")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staffService;
    @GetMapping
    @Operation(
            summary = "병원 직원 목록 조회",
            description = "hospitalId에 소속된 직원 목록을 조회합니다."
    )
    public ResponseEntity<List<StaffInfoResponse>> findAll(@HospitalId UUID hospitalId) {
        return ResponseEntity.ok(staffService.findAllByHospitalId(hospitalId));
    }
}
