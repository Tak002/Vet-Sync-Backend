package com.vetsync.backend.service;

import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.domain.Patient;
import com.vetsync.backend.domain.PatientDayNote;
import com.vetsync.backend.dto.patientDayNote.PatientDayNoteResponse;
import com.vetsync.backend.dto.patientDayNote.PatientDayNoteUpsertRequest;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.repository.PatientDayNoteRepository;
import com.vetsync.backend.repository.PatientRepository;
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
public class PatientDayNoteService {
    private final PatientDayNoteRepository patientDayNoteRepository;
    private final PatientRepository patientRepository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public PatientDayNoteResponse get(UUID hospitalId, UUID patientId, LocalDate date) {
        patientRepository.findById(patientId).orElseThrow(()-> new CustomException(ErrorCode.PATIENT_NOT_FOUND));
        PatientDayNote note = patientDayNoteRepository
                .findByHospital_IdAndPatient_IdAndNoteDate(hospitalId, patientId, date)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND)); // 네 프로젝트 코드에 맞게 변경

        return PatientDayNoteResponse.from(note);
    }

    @Transactional
    public PatientDayNoteResponse upsert(UUID hospitalId, UUID patientId, LocalDate date, @Valid PatientDayNoteUpsertRequest req) {
        patientRepository.findById(patientId).orElseThrow(()-> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        PatientDayNote note = patientDayNoteRepository
                .findByHospital_IdAndPatient_IdAndNoteDate(hospitalId, patientId, date)
                .orElseGet(() -> PatientDayNote.builder()
                        .hospital(entityManager.getReference(Hospital.class, hospitalId))
                        .patient(entityManager.getReference(Patient.class, patientId))
                        .noteDate(date)
                        .content(new HashMap<>())
                        .build());

        // 통째로 덮어쓰기
        note.setContent(req.content());

        PatientDayNote saved = patientDayNoteRepository.save(note);
        return PatientDayNoteResponse.from(saved);
    }
}
