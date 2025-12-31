package com.vetsync.backend.repository;

import com.vetsync.backend.domain.PatientDayNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface PatientDayNoteRepository extends JpaRepository<PatientDayNote, UUID> {
    Optional<PatientDayNote> findByHospital_IdAndPatient_IdAndNoteDate(UUID hospitalId, UUID patientId, LocalDate date);
}
