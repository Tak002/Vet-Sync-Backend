package com.vetsync.backend.repository;

import com.vetsync.backend.domain.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StaffRepository extends JpaRepository<Staff, UUID> {
    Optional<Staff> findByHospital_IdAndLoginId(UUID hospitalId, String loginId);
    boolean existsByHospital_IdAndLoginId(UUID hospitalId, String loginId);

    List<Staff> findAllByHospital_Id(UUID hospitalId);

    boolean existsByIdAndHospital_Id(UUID id, UUID hospitalId);
}
