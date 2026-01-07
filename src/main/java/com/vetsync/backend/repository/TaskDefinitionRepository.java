package com.vetsync.backend.repository;

import com.vetsync.backend.domain.TaskDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskDefinitionRepository  extends JpaRepository<TaskDefinition, UUID> {
    List<TaskDefinition> findAllByHospital_Id(UUID hospitalId);

    Boolean existsByIdAndHospital_Id(UUID id, UUID hospitalId);

    boolean existsByHospital_IdAndNameIgnoreCase(UUID hospitalId, String name);

    boolean existsByHospital_IdAndNameIgnoreCaseAndIdNot(UUID hospitalId, String name, UUID id);
}
