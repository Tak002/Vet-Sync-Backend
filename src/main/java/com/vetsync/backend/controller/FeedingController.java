package com.vetsync.backend.controller;

import com.vetsync.backend.dto.feeding.FeedingResponse;
import com.vetsync.backend.dto.feeding.FeedingUpsertRequest;
import com.vetsync.backend.global.annotation.HospitalId;
import com.vetsync.backend.service.FeedingService;
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
@RequestMapping("/patients/{patientId}/feedings")
@RequiredArgsConstructor
@Tag(name = "Feeding", description = "환자 일일 급여(사료/아침/점심/저녁) 등록/수정/조회 API")
public class FeedingController {
    private final FeedingService feedingService;

    @GetMapping("/{date}")
    @Operation(summary = "일일 급여 조회", description = "해당 환자의 특정 일자 급여 정보를 조회합니다.")
    public ResponseEntity<FeedingResponse> get(
            @HospitalId UUID hospitalId,
            @PathVariable UUID patientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(feedingService.get(hospitalId, patientId, date));
    }

    @PutMapping("/{date}")
    @Operation(summary = "일일 급여 저장(업서트)", description = "해당 환자의 특정 일자 급여 정보를 저장합니다. (없으면 생성, 있으면 덮어쓰기)")
    public ResponseEntity<FeedingResponse> upsert(
            @HospitalId UUID hospitalId,
            @PathVariable UUID patientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Valid @RequestBody FeedingUpsertRequest req
    ) {
        return ResponseEntity.ok(feedingService.upsert(hospitalId, patientId, date, req));
    }
}
