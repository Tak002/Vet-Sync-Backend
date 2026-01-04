package com.vetsync.backend.service;

import com.vetsync.backend.dto.task.TaskDefinitionResponse;
import com.vetsync.backend.global.exception.CustomException;
import com.vetsync.backend.global.exception.ErrorCode;
import com.vetsync.backend.repository.HospitalRepository;
import com.vetsync.backend.repository.TaskDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskDefinitionService {
    private final TaskDefinitionRepository taskDefinitionRepository;
    private final HospitalRepository hospitalRepository;

    @Transactional(readOnly = true)
    public void validateTaskDefinitionAccessible(UUID hospitalId, UUID taskDefinitionId) {
        if(!hospitalRepository.existsById(hospitalId)) {
            throw new CustomException(ErrorCode.HOSPITAL_NOT_FOUND);
        }
        if(!taskDefinitionRepository.existsByIdAndHospital_Id(taskDefinitionId,hospitalId)){
            throw new CustomException(ErrorCode.TASK_DEFINITION_NOT_FOUND);
        }
    }

    @Transactional (readOnly = true)
    public List<TaskDefinitionResponse> getAccessibleTaskDefinitions(UUID hospitalId) {
        if(!hospitalRepository.existsById(hospitalId)) {
            throw new CustomException(ErrorCode.HOSPITAL_NOT_FOUND);
        }
        return taskDefinitionRepository.findAllByHospital_Id(hospitalId).stream()
                .map(TaskDefinitionResponse::from)
                .toList();
    }
}
