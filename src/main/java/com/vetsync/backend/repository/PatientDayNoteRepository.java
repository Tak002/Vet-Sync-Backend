package com.vetsync.backend.repository;

import com.vetsync.backend.domain.PatientDayContextNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface PatientDayNoteRepository extends JpaRepository<PatientDayContextNote, UUID> {
    Optional<PatientDayContextNote> findByHospital_IdAndPatient_IdAndNoteDate(UUID hospitalId, UUID patientId, LocalDate date);
}
