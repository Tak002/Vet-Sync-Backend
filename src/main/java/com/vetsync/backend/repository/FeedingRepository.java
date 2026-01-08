package com.vetsync.backend.repository;

import com.vetsync.backend.domain.Feeding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface FeedingRepository extends JpaRepository<Feeding, UUID> {
    Optional<Feeding> findByHospital_IdAndPatient_IdAndFeedingDate(UUID hospitalId, UUID patientId, LocalDate feedingDate);
}
