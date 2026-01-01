package com.vetsync.backend.repository;

import com.vetsync.backend.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Boolean existsByHospitalIdAndOwnerIdAndName(UUID hospitalId, UUID ownerId,String name);
}
