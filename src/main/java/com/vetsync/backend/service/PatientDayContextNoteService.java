package com.vetsync.backend.service;

import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.domain.Patient;
import com.vetsync.backend.domain.PatientDayContextNote;
import com.vetsync.backend.dto.patientDayNote.PatientDayContextNoteResponse;
import com.vetsync.backend.dto.patientDayNote.PatientDayContextNoteUpsertRequest;
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

    /**
     * Retrieve the context note for a patient on a specific date.
     *
     * @param hospitalId UUID of the hospital the patient belongs to
     * @param patientId  UUID of the patient
     * @param date       the date of the note
     * @return           a PatientDayContextNoteResponse representing the note for the given date
     * @throws CustomException if no context note exists for the provided hospital, patient, and date (ErrorCode.NOT_FOUND)
     */
    @Transactional(readOnly = true)
    public PatientDayContextNoteResponse get(UUID hospitalId, UUID patientId, LocalDate date) {
        patientService.validatePatientAccessible(hospitalId, patientId);

        PatientDayContextNote note = patientDayNoteRepository
                .findByHospital_IdAndPatient_IdAndNoteDate(hospitalId, patientId, date)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND)); // 네 프로젝트 코드에 맞게 변경

        return PatientDayContextNoteResponse.from(note);
    }

    /**
     * Create or update the patient's contextual note for a specific date.
     *
     * Replaces the note's content with the request's content; if a note does not exist one is created.
     *
     * @param hospitalId the hospital UUID that owns the patient
     * @param patientId  the patient UUID for whom the note is stored
     * @param date       the date the note applies to
     * @param req        upsert request whose `content` will replace the note's existing content
     * @return           the saved PatientDayContextNote as a response DTO
     */
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