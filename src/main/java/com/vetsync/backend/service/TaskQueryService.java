package com.vetsync.backend.service;

import com.vetsync.backend.domain.Task;
import com.vetsync.backend.dto.task.TaskInfoResponse;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskQueryService {
    private final TaskRepository taskRepository;
    private final PatientService patientService;

    @Transactional(readOnly = true)
    public TaskInfoResponse getSingleTask(UUID hospitalId, UUID taskId) {
        Task task = getTaskEntity(hospitalId, taskId);
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

    private Task getTaskEntity(UUID hospitalId, UUID taskId){
        return taskRepository.findByIdAndHospital_Id(taskId, hospitalId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
    }
}
