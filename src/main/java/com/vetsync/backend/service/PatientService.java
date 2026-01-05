package com.vetsync.backend.service;

import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.domain.Owner;
import com.vetsync.backend.domain.Patient;
import com.vetsync.backend.domain.Staff;
import com.vetsync.backend.dto.patient.PatientInfoResponse;
import com.vetsync.backend.dto.patient.PatientRegisterRequest;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.repository.HospitalRepository;
import com.vetsync.backend.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final EntityManager entityManager;
    private final HospitalRepository hospitalRepository;

    /**
     * Register a new patient for the given hospital and return the saved patient information.
     *
     * @param hospitalId the UUID of the hospital where the patient is registered
     * @param staffId    the UUID of the staff member performing the registration
     * @param req        the patient registration request containing owner, name, species, breed, and gender
     * @return           a PatientInfoResponse representing the persisted patient
     * @throws CustomException if a patient with the same hospital, owner, and name already exists (ErrorCode.ENTITY_ALREADY_EXISTS)
     */
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

    /**
     * Verifies that the hospital and patient exist and are accessible.
     *
     * @param hospitalId the UUID of the hospital to validate
     * @param patientId  the UUID of the patient to validate
     * @throws CustomException with ErrorCode.HOSPITAL_NOT_FOUND if the hospital does not exist
     * @throws CustomException with ErrorCode.PATIENT_NOT_FOUND  if the patient does not exist
     */
    public void validatePatientAccessible(UUID hospitalId, @NotNull UUID patientId) {
        if (hospitalRepository.findById(hospitalId).isEmpty()) {
            throw new CustomException(ErrorCode.HOSPITAL_NOT_FOUND);
        }
        if (patientRepository.findById(patientId).isEmpty()) {
            throw new CustomException(ErrorCode.PATIENT_NOT_FOUND);
        }
    }

    public  PatientInfoResponse getPatientInfo(UUID hospitalId, UUID patientId) {
        Patient patient = patientRepository.findByIdAndHospitalId(patientId, hospitalId)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));
        return PatientInfoResponse.from(patient);
    }
}