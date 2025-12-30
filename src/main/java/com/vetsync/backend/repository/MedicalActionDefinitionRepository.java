package com.vetsync.backend.repository;

import com.vetsync.backend.domain.MedicalActionDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicalActionDefinitionRepository
        extends JpaRepository<MedicalActionDefinition, UUID> {
}
