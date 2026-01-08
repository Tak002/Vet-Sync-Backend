package com.vetsync.backend.service;

import com.vetsync.backend.domain.Feeding;
import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.domain.Patient;
import com.vetsync.backend.dto.feeding.FeedingResponse;
import com.vetsync.backend.dto.feeding.FeedingUpsertRequest;
import com.vetsync.backend.repository.FeedingRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedingService {
    private final PatientService patientService;
    private final FeedingRepository feedingRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public FeedingResponse get(UUID hospitalId, UUID patientId, LocalDate date) {
        patientService.validatePatientAccessible(hospitalId, patientId);
        return feedingRepository
                .findByHospital_IdAndPatient_IdAndFeedingDate(hospitalId, patientId, date)
                .map(FeedingResponse::from)
                .orElseGet(FeedingResponse::empty);

    }

    @Transactional
    public FeedingResponse upsert(UUID hospitalId, UUID patientId, LocalDate date, @Valid FeedingUpsertRequest req) {
        patientService.validatePatientAccessible(hospitalId, patientId);

        Feeding feeding = feedingRepository
                .findByHospital_IdAndPatient_IdAndFeedingDate(hospitalId, patientId, date)
                .orElseGet(() -> Feeding.builder()
                        .hospital(entityManager.getReference(Hospital.class, hospitalId))
                        .patient(entityManager.getReference(Patient.class, patientId))
                        .feedingDate(date)
                        .diet("")
                        .breakfastMenu("")
                        .breakfastStatus((short) 3)
                        .lunchMenu("")
                        .lunchStatus((short) 3)
                        .dinnerMenu("")
                        .dinnerStatus((short) 3)
                        .build());

        // 요청 값이 null이 아니면 덮어쓰기, null이면 기존 값 유지
        if (req.diet() != null) feeding.setDiet(req.diet());
        if (req.breakfastMenu() != null) feeding.setBreakfastMenu(req.breakfastMenu());
        if (req.breakfastStatus() != null) feeding.setBreakfastStatus(req.breakfastStatus().shortValue());
        if (req.lunchMenu() != null) feeding.setLunchMenu(req.lunchMenu());
        if (req.lunchStatus() != null) feeding.setLunchStatus(req.lunchStatus().shortValue());
        if (req.dinnerMenu() != null) feeding.setDinnerMenu(req.dinnerMenu());
        if (req.dinnerStatus() != null) feeding.setDinnerStatus(req.dinnerStatus().shortValue());

        Feeding saved = feedingRepository.save(feeding);
        return FeedingResponse.from(saved);
    }
}
