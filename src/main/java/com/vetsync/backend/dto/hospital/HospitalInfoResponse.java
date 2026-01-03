package com.vetsync.backend.dto.hospital;

import com.vetsync.backend.domain.Hospital;

import java.util.UUID;

public record HospitalInfoResponse(
        UUID id,
        String name
) {
    public static HospitalInfoResponse from(Hospital hospital) {
        return new HospitalInfoResponse(hospital.getId(), hospital.getName());
    }
}
