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
    private final TaskDefinitionService taskDefinitionService;

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

    /**
     * Create a patient day task definition note for the given hospital, patient, date, and task definition, or fill an existing empty note.
     *
     * @param hospitalId the hospital UUID
     * @param patientId  the patient UUID
     * @param taskDate   the date of the task
     * @param request    create request containing the taskDefinitionId and content
     * @return           a response representing the created or updated PatientDayTaskDefinitionNote
     * @throws CustomException with ErrorCode.ENTITY_ALREADY_EXISTS if a non-empty note for the same hospital, patient, date, and task definition already exists
     */
    @Transactional
    PatientDayTaskDefinitionNoteInfoResponse createDefinitionNote(UUID hospitalId, UUID patientId, LocalDate taskDate, PatientDayTaskDefinitionNoteCreateRequest request) {
        // 요청 값 검증
        patientService.validatePatientAccessible(hospitalId, patientId);
        taskDefinitionService.validateTaskDefinitionAccessible(hospitalId, request.taskDefinitionId());
        
        PatientDayTaskDefinitionNote already = patientDayTaskDefinitionNoteRepository.getByHospital_IdAndPatient_IdAndTaskDateAndTaskDefinition_Id(hospitalId, patientId, taskDate, request.taskDefinitionId());
        // 이미 note가 존재하고, note 내용이 비어있지 않은 경우에는 중복 생성 불가
        if(already != null){
            if(!already.getContent().isBlank()){
                throw new CustomException(ErrorCode.ENTITY_ALREADY_EXISTS);
            }
            // 이미 note가 존재하지만, note 내용이 비어있는 경우에는 update로 처리
            return updateDefinitionNote(hospitalId, patientId, taskDate, already.getId(),
                    new PatientDayTaskDefinitionNoteUpdateRequest(request.content())
            );
        }

        PatientDayTaskDefinitionNote note = PatientDayTaskDefinitionNote.builder()
                .hospital(entityManager.getReference(Hospital.class, hospitalId))
                .patient(entityManager.getReference(Patient.class, patientId))
                .taskDate(taskDate)
                .taskDefinition(entityManager.getReference(TaskDefinition.class, request.taskDefinitionId()))
                .content(request.content())
                .build();
        return PatientDayTaskDefinitionNoteInfoResponse.from(patientDayTaskDefinitionNoteRepository.save(note));
    }

    // definition note 수정
    // ""(빈 문자열)로 수정하는 것도 허용
    /**
     * Updates the content of an existing patient day task definition note identified by its ID and context.
     *
     * If the request's `content` is `null`, the note's content is left unchanged. If non-null, the value is trimmed
     * and stored (an empty string is allowed). The method validates patient access and that the note exists for the
     * specified hospital, patient, and task date.
     *
     * @param hospitalId the hospital's UUID
     * @param patientId the patient's UUID
     * @param taskDate the task date of the note
     * @param noteId the UUID of the note to update
     * @param request the update request containing the new `content` (may be `null` to indicate no change)
     * @return a response DTO representing the updated note
     * @throws CustomException with ErrorCode.TASK_DEFINITION_NOTE_NOT_FOUND if no matching note is found
     */
    @Transactional
    public  PatientDayTaskDefinitionNoteInfoResponse updateDefinitionNote(UUID hospitalId, UUID patientId, LocalDate taskDate, UUID noteId, PatientDayTaskDefinitionNoteUpdateRequest request) {
        patientService.validatePatientAccessible(hospitalId, patientId);

        // 수정할 note 조회
        // 본래 id와 hospitalId 만으로도 조회가 가능하나, 추가로 patientId와 taskDate 조건을 넣어 안전성 강화
        PatientDayTaskDefinitionNote note = patientDayTaskDefinitionNoteRepository.findByIdAndHospital_IdAndPatient_IdAndTaskDate(noteId, hospitalId, patientId, taskDate)
                .orElseThrow(()-> new CustomException(ErrorCode.TASK_DEFINITION_NOTE_NOT_FOUND));
        if (request.content() != null) {
            note.setContent(request.content().trim());
        }
        return PatientDayTaskDefinitionNoteInfoResponse.from(note);
    }
}