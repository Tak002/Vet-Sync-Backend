package com.vetsync.backend.repository;

import com.vetsync.backend.domain.Patient;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID>, JpaSpecificationExecutor<Patient> {
    Boolean existsByHospitalIdAndOwnerIdAndName(UUID hospitalId, UUID ownerId,String name);


    boolean existsByIdAndHospitalId(@NotNull UUID patientId, @NotNull UUID hospitalId);

    Optional<Patient> findByIdAndHospitalId(UUID patientId, UUID hospitalId);
}
