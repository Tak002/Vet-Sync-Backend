package com.vetsync.backend.service;

import com.vetsync.backend.dto.staff.StaffInfoResponse;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;

    @Transactional(readOnly = true)
    public List<StaffInfoResponse> findAllByHospitalId(UUID hospitalId) {
        return staffRepository.findAllByHospital_Id(hospitalId).stream()
                .map(StaffInfoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public void validateStaffId(UUID id, UUID hospitalId) {
        if(id == null || !staffRepository.existsByIdAndHospital_Id(id,hospitalId)){
            throw new CustomException(ErrorCode.INVALID_STAFF_ID);
        }
    }
}
