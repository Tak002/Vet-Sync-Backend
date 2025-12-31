package com.vetsync.backend.service;

import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.domain.Owner;
import com.vetsync.backend.domain.Staff;
import com.vetsync.backend.dto.owner.OwnerInfoResponse;
import com.vetsync.backend.dto.owner.OwnerRegisterRequest;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.repository.OwnerRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final EntityManager entityManager;

    @Transactional
    public OwnerInfoResponse registerOwner(UUID hospitalId, UUID staffId, OwnerRegisterRequest req) {
        if(ownerRepository.existsByHospitalIdAndPhone(hospitalId,req.phone())){
            throw new CustomException(ErrorCode.OWNER_ALREADY_EXISTS);
        }

        Owner build = Owner.builder()
                .hospital(entityManager.getReference(Hospital.class, hospitalId))
                .name(req.name())
                .phone(req.phone())
                .email(req.email())
                .address(req.address())
                .memo(req.memo())
                .createdBy(entityManager.getReference(Staff.class, staffId))
                .build();

        Owner save = ownerRepository.save(build);
        return OwnerInfoResponse.from(save);
    }
}
