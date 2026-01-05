package com.vetsync.backend.repository;

import com.vetsync.backend.domain.PatientDayTaskDefinitionNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientDayTaskDefinitionNoteRepository extends JpaRepository<PatientDayTaskDefinitionNote, UUID> {
    List<PatientDayTaskDefinitionNote> getByHospital_IdAndPatient_IdAndTaskDate(UUID hospitalId, UUID patientId, LocalDate taskDate);

    PatientDayTaskDefinitionNote getByHospital_IdAndPatient_IdAndTaskDateAndTaskDefinition_Id(UUID hospitalId, UUID patientId, LocalDate taskDate, UUID taskDefinitionId);

    Optional<PatientDayTaskDefinitionNote> findByIdAndHospital_IdAndPatient_IdAndTaskDate(UUID noteId, UUID hospitalId, UUID patientId, LocalDate taskDate);
}
