package com.vetsync.backend.service;

import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.domain.Owner;
import com.vetsync.backend.domain.Patient;
import com.vetsync.backend.domain.Staff;
import com.vetsync.backend.dto.patient.PatientInfoResponse;
import com.vetsync.backend.dto.patient.PatientRegisterRequest;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final EntityManager entityManager;

    @Transactional
    public PatientInfoResponse registerPatient(
            UUID hospitalId,
            UUID staffId,
            PatientRegisterRequest req
    ) {
        // Patient 생성
        if(patientRepository.existsByHospitalIdAndOwnerIdAndName(hospitalId,req.ownerId(), req.name())){
            throw new CustomException(ErrorCode.ENTITY_ALREADY_EXISTS);
        }

        Patient patient = Patient.builder()
                .hospital(entityManager.getReference(Hospital.class, hospitalId))
                .owner(entityManager.getReference(Owner.class, req.ownerId()))
                .name(req.name())
                .species(req.species())
                .speciesDetail(req.speciesDetail())
                .breed(req.breed())
                .gender(req.gender())
                .createdBy(entityManager.getReference(Staff.class, staffId))
                .build();

        Patient saved = patientRepository.save(patient);

        return  PatientInfoResponse.from(saved);
    }
}
