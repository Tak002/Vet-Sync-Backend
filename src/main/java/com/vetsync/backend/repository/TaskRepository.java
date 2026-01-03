package com.vetsync.backend.repository;

import com.vetsync.backend.domain.Task;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findByIdAndHospital_Id(UUID taskId, UUID hospitalId);

    List<Task> findAllByHospital_IdAndPatient_IdAndTaskDate(UUID hospitalId, UUID patientId, LocalDate taskDate);

    List<Task> findAllByHospital_IdAndPatient_Id(UUID hospitalId, UUID patientId);

    boolean existsByHospital_IdAndPatient_IdAndTaskDateAndTaskHourAndTaskDefinition_Id(UUID hospitalId, @NotNull UUID patientId, @NotNull LocalDate taskDate, @NotNull @Min(0) @Max(23) Integer taskHour, @NotNull UUID taskDefinitionId);
}
