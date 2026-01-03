package com.vetsync.backend.repository;

import com.vetsync.backend.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HospitalRepository extends JpaRepository<Hospital, UUID> {
    boolean existsByName(String hospitalName);
}
