package com.vetsync.backend.contorller;

import com.vetsync.backend.dto.staff.StaffInfoResponse;
import com.vetsync.backend.dto.staff.StaffRegisterRequest;
import com.vetsync.backend.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Staffs", description = "스태프 등록/관리 API")
@RestController
@RequestMapping("/staffs")
@RequiredArgsConstructor
public class StaffController {
    private final StaffService staffService;

    @PostMapping
    @Operation(summary = "스태프 등록", description = "새로운 스태프를 등록합니다")
    public ResponseEntity<StaffInfoResponse> registerOwner(@Valid @RequestBody StaffRegisterRequest staffRegisterRequest) {
        StaffInfoResponse staffInfoResponse = staffService.registerStaff(staffRegisterRequest);
        return ResponseEntity.ok(staffInfoResponse);
    }

}
