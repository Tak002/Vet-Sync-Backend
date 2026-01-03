package com.vetsync.backend.repository;

import com.vetsync.backend.domain.TaskDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskDefinitionRepository  extends JpaRepository<TaskDefinition, UUID> {
    List<TaskDefinition> findAllByHospitalIdOrIsGlobalTrue(UUID hospitalId);
}
