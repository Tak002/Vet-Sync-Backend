package com.vetsync.backend.repository;

import com.vetsync.backend.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    boolean existsByHospital_IdAndPatient_IdAndTaskDateAndTaskHour(UUID hospitalId, UUID patientId, LocalDate taskDate, Integer taskHour);

    Optional<Task> findByIdAndHospital_Id(UUID taskId, UUID hospitalId);

    List<Task> findAllByHospital_IdAndPatient_IdAndTaskDate(UUID hospitalId, UUID patientId, LocalDate taskDate);

    List<Task> findAllByHospital_IdAndPatient_Id(UUID hospitalId, UUID patientId);
}
