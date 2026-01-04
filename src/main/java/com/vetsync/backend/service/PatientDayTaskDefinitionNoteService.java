package com.vetsync.backend.service;

import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.domain.Patient;
import com.vetsync.backend.domain.PatientDayTaskDefinitionNote;
import com.vetsync.backend.domain.TaskDefinition;
import com.vetsync.backend.dto.patientDayTaskDefinitionNote.PatientDayTaskDefinitionNoteCreateRequest;
import com.vetsync.backend.dto.patientDayTaskDefinitionNote.PatientDayTaskDefinitionNoteInfoResponse;
import com.vetsync.backend.dto.patientDayTaskDefinitionNote.PatientDayTaskDefinitionNoteUpdateRequest;
import com.vetsync.backend.repository.PatientDayTaskDefinitionNoteRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientDayTaskDefinitionNoteService {
    private final PatientDayTaskDefinitionNoteRepository patientDayTaskDefinitionNoteRepository;
    private final PatientService patientService;
    private final EntityManager entityManager;

    // 특정 날짜에 대한 notes 목록 조회
    @Transactional(readOnly = true)
    public List<PatientDayTaskDefinitionNoteInfoResponse> getDefinitionNotes(UUID hospitalId, UUID patientId, LocalDate taskDate) {
        patientService.validatePatientAccessible(hospitalId, patientId);

        return patientDayTaskDefinitionNoteRepository.getByHospital_IdAndPatient_IdAndTaskDate(hospitalId, patientId, taskDate)
                .stream()
                .map(PatientDayTaskDefinitionNoteInfoResponse::from)
                .toList();
    }

    // 특정 definition에 대한 note 조회
    // note 가 존재하지 않을 경우 null 반환
    @Transactional(readOnly = true)
    public PatientDayTaskDefinitionNote findByContext(UUID hospitalId, UUID patientId, LocalDate taskDate, UUID taskDefinitionId) {
        patientService.validatePatientAccessible(hospitalId, patientId);
        return patientDayTaskDefinitionNoteRepository.getByHospital_IdAndPatient_IdAndTaskDateAndTaskDefinition_Id(hospitalId, patientId, taskDate, taskDefinitionId);
    }

    // definition note 생성 및 관련 task들과 연결
    @Transactional
    PatientDayTaskDefinitionNote createDefinitionNote(UUID hospitalId, UUID patientId, LocalDate taskDate, PatientDayTaskDefinitionNoteCreateRequest request) {
        patientService.validatePatientAccessible(hospitalId, patientId);

        PatientDayTaskDefinitionNote note = PatientDayTaskDefinitionNote.builder()
                .hospital(entityManager.getReference(Hospital.class, hospitalId))
                .patient(entityManager.getReference(Patient.class, patientId))
                .taskDate(taskDate)
                .taskDefinition(entityManager.getReference(TaskDefinition.class, request.taskDefinitionId()))
                .note(request.note())
                .build();
        return patientDayTaskDefinitionNoteRepository.save(note);
    }

    // definition note 수정
    // null로 바뀔경우 task도 null로 변경
    @Transactional
    public  PatientDayTaskDefinitionNoteInfoResponse updateDefinitionNote(UUID hospitalId, UUID patientId, LocalDate taskDate, UUID noteId, PatientDayTaskDefinitionNoteUpdateRequest request) {
        patientService.validatePatientAccessible(hospitalId, patientId);
        return null;
    }
}
