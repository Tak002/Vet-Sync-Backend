package com.vetsync.backend.service;

import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.dto.hospital.HospitalInfoResponse;
import com.vetsync.backend.dto.hospital.HospitalRegisterRequest;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HospitalService {
    private final HospitalRepository hospitalRepository;

    public HospitalInfoResponse registerHospital(HospitalRegisterRequest req) {
        if(hospitalRepository.existsByName(req.name())) {
            throw new CustomException(ErrorCode.ENTITY_ALREADY_EXISTS);
        }
        Hospital hospital = Hospital.builder().name(req.name()).build();
        Hospital saved = hospitalRepository.save(hospital);
        return HospitalInfoResponse.from(saved);
    }
}
