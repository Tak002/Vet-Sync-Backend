package com.vetsync.backend.controller;

import com.vetsync.backend.dto.owner.OwnerInfoResponse;
import com.vetsync.backend.dto.owner.OwnerRegisterRequest;
import com.vetsync.backend.global.annotation.HospitalId;
import com.vetsync.backend.global.annotation.StaffId;
import com.vetsync.backend.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Owner", description = "반려인 등록/관리 API")
@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @PostMapping
    @Operation(summary = "반려인 등록", description = "새로운 반려인을 등록합니다")
    public ResponseEntity<OwnerInfoResponse> registerOwner(@HospitalId UUID hospitalId, @StaffId UUID staffId,
                                                           @Valid @RequestBody OwnerRegisterRequest ownerRegisterRequest) {
        OwnerInfoResponse ownerInfoResponse = ownerService.registerOwner(hospitalId,staffId, ownerRegisterRequest);
        return ResponseEntity.ok(ownerInfoResponse);
    }

}
