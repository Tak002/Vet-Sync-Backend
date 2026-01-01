package com.vetsync.backend.repository;

import com.vetsync.backend.domain.TaskDefinition;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskDefinitionRepository  extends JpaRepository<TaskDefinition, UUID> {
    boolean existsByIdAndHospital_Id(@NotNull UUID uuid, @NotNull UUID hospitalId);
}
