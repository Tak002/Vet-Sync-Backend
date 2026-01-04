package com.vetsync.backend.service;

import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.domain.Patient;
import com.vetsync.backend.domain.PatientDayTaskDefinitionNote;
import com.vetsync.backend.domain.TaskDefinition;
import com.vetsync.backend.dto.patientDayTaskDefinitionNote.PatientDayTaskDefinitionNoteCreateRequest;
import com.vetsync.backend.dto.patientDayTaskDefinitionNote.PatientDayTaskDefinitionNoteInfoResponse;
import com.vetsync.backend.dto.patientDayTaskDefinitionNote.PatientDayTaskDefinitionNoteUpdateRequest;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
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
    // ""(빈 문자열)로 수정하는 것도 허용
    // null로 수정하는 것은 허용하지 않음 (변경 없음 정책)
    @Transactional
    public  PatientDayTaskDefinitionNoteInfoResponse updateDefinitionNote(UUID hospitalId, UUID patientId, LocalDate taskDate, UUID noteId, PatientDayTaskDefinitionNoteUpdateRequest request) {
        patientService.validatePatientAccessible(hospitalId, patientId);

        // 수정할 note 조회
        // 본래 id와 hospitalId 만으로도 조회가 가능하나, 추가로 patientId와 taskDate 조건을 넣어 안전성 강화
        PatientDayTaskDefinitionNote note = patientDayTaskDefinitionNoteRepository.findByIdAndHospital_IdAndPatient_IdAndTaskDate(noteId, hospitalId, patientId, taskDate)
                .orElseThrow(()-> new CustomException(ErrorCode.TASK_DEFINITION_NOTE_NOT_FOUND));
        if (request.note() != null) {
            note.setNote(request.note());
        }
        return PatientDayTaskDefinitionNoteInfoResponse.from(note);
    }
}
