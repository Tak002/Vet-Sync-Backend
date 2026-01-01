package com.vetsync.backend.service;

import com.vetsync.backend.domain.*;
import com.vetsync.backend.dto.task.TaskCreateRequest;
import com.vetsync.backend.dto.task.TaskInfoResponse;
import com.vetsync.backend.dto.task.TaskStatusChangeRequest;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.repository.TaskRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final PatientService patientService;
    private final TaskDefinitionService taskDefinitionService;
    private final EntityManager entityManager;

    // Create Task
    @Transactional
    public TaskInfoResponse create(UUID hospitalId, UUID staffId, TaskCreateRequest req) {
        // 병원에 존재하지 않는 환자
        patientService.validatePatientAccessible(hospitalId, req.patientId());
        // 병원에 존재하지 않는 업무 정의
        taskDefinitionService.validateTaskDefinitionAccessible(hospitalId, req.taskDefinitionId());

        // 중복된 업무 일정
        if(taskRepository.existsByHospital_IdAndPatient_IdAndTaskDateAndTaskHour(hospitalId, req.patientId(), req.taskDate(), req.taskHour())){
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

        Task saved = taskRepository.save(task);
        return TaskInfoResponse.from(saved);
    }

    @Transactional
    public TaskInfoResponse changeStatus(UUID hospitalId, UUID taskId, TaskStatusChangeRequest req) {
        Task task = taskRepository.findByIdAndHospital_Id(taskId, hospitalId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND)); // or TASK_NOT_FOUND


        // result 반영 (요구사항: 상태변화 요청에 포함)
        task.setResult(req.result());
        if( req.result()!= null && !req.result().isBlank()){
            task.setStatus(req.status());
        }

        // save 호출 없어도 @Transactional이면 dirty checking으로 반영됨
        return TaskInfoResponse.from(task);
    }

    @Transactional(readOnly = true)
    public TaskInfoResponse getSingleTask(UUID hospitalId, UUID taskId) {
        Task task = taskRepository.findByIdAndHospital_Id(taskId, hospitalId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND)); // or TASK_NOT_FOUND
        return TaskInfoResponse.from(task);
    }

    @Transactional(readOnly = true)
    public List<TaskInfoResponse> getPatientTasks(UUID hospitalId, UUID patientId) {
        patientService.validatePatientAccessible(hospitalId,patientId);

        return taskRepository.findAllByHospital_IdAndPatient_Id(hospitalId, patientId)
                .stream()
                .map(TaskInfoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TaskInfoResponse> getPatientDayTasks(UUID hospitalId, UUID patientId, LocalDate taskDate) {
        patientService.validatePatientAccessible(hospitalId,patientId);

        return taskRepository
                .findAllByHospital_IdAndPatient_IdAndTaskDate(hospitalId, patientId, taskDate)
                .stream()
                .map(TaskInfoResponse::from)
                .toList();
    }
}
