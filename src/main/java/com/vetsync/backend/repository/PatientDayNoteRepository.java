package com.vetsync.backend.repository;

import com.vetsync.backend.domain.PatientDayContextNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface PatientDayNoteRepository extends JpaRepository<PatientDayContextNote, UUID> {
    /**
 * Retrieve the patient-day context note for a specific hospital, patient, and date.
 *
 * @param hospitalId the UUID of the hospital
 * @param patientId  the UUID of the patient
 * @param date       the note's date
 * @return an Optional containing the matching PatientDayContextNote if present, empty otherwise
 */
Optional<PatientDayContextNote> findByHospital_IdAndPatient_IdAndNoteDate(UUID hospitalId, UUID patientId, LocalDate date);
}