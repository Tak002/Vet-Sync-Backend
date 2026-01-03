package com.vetsync.backend.service;

import com.vetsync.backend.dto.staff.StaffInfoResponse;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.repository.StaffRepository;
import com.vetsync.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;

    public @Nullable List<StaffInfoResponse> findAllByHospitalId(UUID hospitalId) {
        return staffRepository.findAllByHospital_Id(hospitalId).stream()
                .map(StaffInfoResponse::from)
                .toList();
    }

    public void validateStaffId(UUID id, UUID hospitalId) {
        if(!staffRepository.existsByIdAndHospital_Id(id,hospitalId)){
            throw new CustomException(ErrorCode.INVALID_STAFF_ID);
        }
    }
}
