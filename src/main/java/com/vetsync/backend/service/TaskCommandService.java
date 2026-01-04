package com.vetsync.backend.service;

import com.vetsync.backend.domain.*;
import com.vetsync.backend.dto.task.TaskCreateRequest;
import com.vetsync.backend.dto.task.TaskInfoResponse;
import com.vetsync.backend.dto.task.TaskStatusChangeRequest;
import com.vetsync.backend.dto.task.TaskUpdateRequest;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.repository.TaskRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskCommandService {
    private final TaskRepository taskRepository;
    private final PatientService patientService;
    private final TaskDefinitionService taskDefinitionService;
    private final EntityManager entityManager;
    private final StaffService staffService;

    // Create Task
    @Transactional
    Task createInternal(UUID hospitalId, UUID staffId, TaskCreateRequest req) {
        // 병원에 존재하지 않는 환자
        patientService.validatePatientAccessible(hospitalId, req.patientId());
        // 병원에 존재하지 않는 업무 정의
        taskDefinitionService.validateTaskDefinitionAccessible(hospitalId, req.taskDefinitionId());

        // 중복된 업무 일정
        if(taskRepository.existsByHospital_IdAndPatient_IdAndTaskDateAndTaskHourAndTaskDefinition_Id(hospitalId, req.patientId(), req.taskDate(), req.taskHour(),req.taskDefinitionId())){
            throw new CustomException(ErrorCode.ENTITY_ALREADY_EXISTS);
        }

        Task task = Task.builder()
                .hospital(entityManager.getReference(Hospital.class, hospitalId))
                .patient(entityManager.getReference(Patient.class, req.patientId()))
                .taskDate(req.taskDate())
                .taskHour(req.taskHour())
                .taskDefinition(entityManager.getReference(TaskDefinition.class, req.taskDefinitionId()))
                .taskNotes(req.taskNotes())
                .createdBy(entityManager.getReference(Staff.class, staffId))
                .assignee(req.assigneeId() == null ? null : entityManager.getReference(Staff.class, req.assigneeId()))
                .build();

        return taskRepository.save(task);
    }

    @Transactional
    public TaskInfoResponse changeStatus(UUID hospitalId, UUID taskId, TaskStatusChangeRequest req) {
        Task task = getTaskEntity(hospitalId, taskId);

        task.setStatus(req.status());

        // PreUpdate 호출 위해 flush
        taskRepository.flush();
        return TaskInfoResponse.from(task);
    }

    @Transactional
    public TaskInfoResponse updateTask(UUID hospitalId, UUID taskId, TaskUpdateRequest req) {
        Task task = getTaskEntity(hospitalId, taskId);

        // notes
        // null 일경우 변경 x
        if (req.taskNotes() != null) {
            String notes = req.taskNotes().trim();
            // 빈 문자열 일 경우 결과 해제
            task.setTaskNotes(notes.isBlank() ? null : notes);
        }

        // result
        // null 일경우 변경 x
        if (req.result() != null) {
            String result = req.result().trim();
            // 빈 문자열 일 경우 결과 해제
            task.setResult(result.isBlank() ? null : result);
        }

        // assignee
        // null 일경우 변경 x
        log.info("AssigneeId: {}", req.assigneeId());
        if (req.assigneeId() != null) {
            // 빈 문자열 일 경우 담당자 해제
            if(req.assigneeId().isBlank()) {
                task.setAssignee(null);
                log.info("Assignee cleared");
            }else{
                UUID assigneeId;
                try{
                    // String 으로 온 assigneeId 를 UUID 로 변환
                    assigneeId = UUID.fromString(req.assigneeId().trim());
                }catch (IllegalArgumentException  e) {
                    throw new CustomException(ErrorCode.INVALID_STAFF_ID);
                }
                // 유효한 직원인지 검증
                staffService.validateStaffId(assigneeId, hospitalId);
                log.info("Assignee set to: {}", assigneeId);

                task.setAssignee(entityManager.getReference(Staff.class, assigneeId));
            }
        }

        // PreUpdate 호출 위해 flush
        taskRepository.flush();

        return TaskInfoResponse.from(task);
    }


    private Task getTaskEntity(UUID hospitalId, UUID taskId){
        return taskRepository.findByIdAndHospital_Id(taskId, hospitalId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
    }

    @Transactional(propagation = Propagation.MANDATORY) // 트랜잭션이 이미 존재해야 함, PatientDayTaskDefinitionNoteService 에서 사용
    public void linkTasksToDefinitionNote(UUID hospitalId, UUID patientId, LocalDate taskDate,
                                          UUID taskDefinitionId, PatientDayTaskDefinitionNote note) {
        // 해당하는 task들을 찾아서 patientDayTaskDefinitionNote 설정
        taskRepository.findByHospital_IdAndPatient_IdAndTaskDateAndTaskDefinition_Id(
                hospitalId, patientId, taskDate, taskDefinitionId
        ).forEach(task -> task.setPatientDayTaskDefinitionNote(note));
    }

}
