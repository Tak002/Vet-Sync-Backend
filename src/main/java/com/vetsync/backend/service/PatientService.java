package com.vetsync.backend.service;

import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.domain.Owner;
import com.vetsync.backend.domain.Patient;
import com.vetsync.backend.domain.Staff;
import com.vetsync.backend.dto.patient.PatientDiagnosisUpdateRequest;
import com.vetsync.backend.dto.patient.PatientInfoResponse;
import com.vetsync.backend.dto.patient.PatientRegisterRequest;
import com.vetsync.backend.global.enums.PatientGender;
import com.vetsync.backend.global.enums.PatientSpecies;
import com.vetsync.backend.global.enums.PatientStatus;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.repository.HospitalRepository;
import com.vetsync.backend.repository.PatientRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final EntityManager entityManager;
    private final HospitalRepository hospitalRepository;

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

    @Transactional(readOnly = true)
    public void validatePatientAccessible(UUID hospitalId, @NotNull UUID patientId) {
        if (hospitalRepository.findById(hospitalId).isEmpty()) {
            throw new CustomException(ErrorCode.HOSPITAL_NOT_FOUND);
        }
        if (patientRepository.findById(patientId).isEmpty()) {
            throw new CustomException(ErrorCode.PATIENT_NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public  PatientInfoResponse getPatientInfo(UUID hospitalId, UUID patientId) {
        Patient patient = patientRepository.findByIdAndHospitalId(patientId, hospitalId)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));
        return PatientInfoResponse.from(patient);
    }

    @Transactional
    public PatientInfoResponse updateDiagnosis(UUID hospitalId, UUID patientId, PatientDiagnosisUpdateRequest req) {
        Patient patient = patientRepository.findByIdAndHospitalId(patientId, hospitalId)
                .orElseThrow(() -> new CustomException(ErrorCode.PATIENT_NOT_FOUND));

        if (req.cc() != null) {
            patient.setCc(req.cc());
        }
        if (req.diagnosis() != null) {
            patient.setDiagnosis(req.diagnosis());
        }

        // JPA dirty checking will update on transaction commit
        return PatientInfoResponse.from(patient);
    }

    @Transactional(readOnly = true)
    public List<PatientInfoResponse> listPatients(
            UUID hospitalId,
            PatientStatus status,
            PatientSpecies species,
            PatientGender gender,
            UUID ownerId,
            String nameKeyword
    ) {
        // 병원 존재 여부는 별도 검증 메서드 재사용 가능하나, 조회 자체는 조건에 병원 ID를 포함
        Specification<Patient> spec = (root, query, cb) -> cb.equal(root.get("hospital").get("id"), hospitalId);

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (species != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("species"), species));
        }
        if (gender != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("gender"), gender));
        }
        if (ownerId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("owner").get("id"), ownerId));
        }
        if (nameKeyword != null && !nameKeyword.isBlank()) {
            String raw = nameKeyword.trim().toLowerCase();

            // LIKE 특수문자 이스케이프 (% _ \)
            String escaped = raw
                    .replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");

            String like = "%" + escaped + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), like.toLowerCase()));
        }

        List<Patient> patients = patientRepository.findAll(spec, Sort.by(Sort.Direction.DESC, "createdAt"));
        return patients.stream().map(PatientInfoResponse::from).collect(Collectors.toList());
    }
}
