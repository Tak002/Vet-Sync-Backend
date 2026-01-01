package com.vetsync.backend.repository;

import com.vetsync.backend.domain.Patient;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Boolean existsByHospitalIdAndOwnerIdAndName(UUID hospitalId, UUID ownerId,String name);

    boolean existsByIdAndHospital_Id(@NotNull UUID uuid, @NotNull UUID hospitalId);
}
