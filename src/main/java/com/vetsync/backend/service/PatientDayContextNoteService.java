package com.vetsync.backend.service;

import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.domain.Patient;
import com.vetsync.backend.domain.PatientDayContextNote;
import com.vetsync.backend.dto.patientDayContextNote.PatientDayContextNoteResponse;
import com.vetsync.backend.dto.patientDayContextNote.PatientDayContextNoteUpsertRequest;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.repository.PatientDayNoteRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientDayContextNoteService {
    private final PatientService patientService;
    private final PatientDayNoteRepository patientDayNoteRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public PatientDayContextNoteResponse get(UUID hospitalId, UUID patientId, LocalDate date) {
        patientService.validatePatientAccessible(hospitalId, patientId);

        PatientDayContextNote note = patientDayNoteRepository
                .findByHospital_IdAndPatient_IdAndNoteDate(hospitalId, patientId, date)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND)); // 네 프로젝트 코드에 맞게 변경

        return PatientDayContextNoteResponse.from(note);
    }

    @Transactional
    public PatientDayContextNoteResponse upsert(UUID hospitalId, UUID patientId, LocalDate date, @Valid PatientDayContextNoteUpsertRequest req) {
        patientService.validatePatientAccessible(hospitalId, patientId);

        PatientDayContextNote note = patientDayNoteRepository
                .findByHospital_IdAndPatient_IdAndNoteDate(hospitalId, patientId, date)
                .orElseGet(() -> PatientDayContextNote.builder()
                        .hospital(entityManager.getReference(Hospital.class, hospitalId))
                        .patient(entityManager.getReference(Patient.class, patientId))
                        .noteDate(date)
                        .content(new HashMap<>())
                        .build());

        // 통째로 덮어쓰기
        note.setContent(req.content());

        PatientDayContextNote saved = patientDayNoteRepository.save(note);
        return PatientDayContextNoteResponse.from(saved);
    }
}
