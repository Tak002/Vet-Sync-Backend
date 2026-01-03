package com.vetsync.backend.repository;

import com.vetsync.backend.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OwnerRepository extends JpaRepository<Owner, UUID> {
    Boolean existsByHospitalIdAndPhone(UUID hospital, String phone);
}
