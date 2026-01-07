package com.vetsync.backend.service;

import com.vetsync.backend.domain.Hospital;
import com.vetsync.backend.domain.TaskDefinition;
import com.vetsync.backend.dto.task.TaskDefinitionCreateRequest;
import com.vetsync.backend.dto.task.TaskDefinitionResponse;
import com.vetsync.backend.dto.task.TaskDefinitionUpdateRequest;
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

    @Transactional
    public TaskDefinitionResponse create(UUID hospitalId, TaskDefinitionCreateRequest request) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new CustomException(ErrorCode.HOSPITAL_NOT_FOUND));

        // 이름 정규화 (trim) 및 중복 검사 (대소문자 무시)
        String normalizedName = request.name() == null ? null : request.name().trim();
        if (normalizedName == null || normalizedName.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "name은 공백일 수 없습니다.");
        }
        if (taskDefinitionRepository.existsByHospital_IdAndNameIgnoreCase(hospitalId, normalizedName)) {
            throw new CustomException(ErrorCode.ENTITY_ALREADY_EXISTS, "동일한 이름의 업무 정의가 이미 존재합니다.");
        }

        TaskDefinition entity = new TaskDefinition();
        entity.setHospital(hospital);
        entity.setName(normalizedName);
        entity.setFixed(request.fixed());
        entity.setDescription(request.description());
        entity.setOptions(request.options());

        TaskDefinition saved = taskDefinitionRepository.save(entity);
        return TaskDefinitionResponse.from(saved);
    }

    @Transactional
    public TaskDefinitionResponse update(UUID hospitalId, UUID taskDefinitionId, TaskDefinitionUpdateRequest request) {
        validateTaskDefinitionAccessible(hospitalId, taskDefinitionId);

        TaskDefinition entity = taskDefinitionRepository.findById(taskDefinitionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_DEFINITION_NOT_FOUND));

        String normalizedName = request.name() == null ? null : request.name().trim();
        if (normalizedName == null || normalizedName.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "name은 공백일 수 없습니다.");
        }
        if (taskDefinitionRepository.existsByHospital_IdAndNameIgnoreCaseAndIdNot(hospitalId, normalizedName, taskDefinitionId)) {
            throw new CustomException(ErrorCode.ENTITY_ALREADY_EXISTS, "동일한 이름의 업무 정의가 이미 존재합니다.");
        }

        entity.setName(normalizedName);
        entity.setFixed(request.fixed());
        entity.setDescription(request.description());
        entity.setOptions(request.options());

        return TaskDefinitionResponse.from(entity);
    }

    @Transactional
    public void delete(UUID hospitalId, UUID taskDefinitionId) {
        validateTaskDefinitionAccessible(hospitalId, taskDefinitionId);
        taskDefinitionRepository.deleteById(taskDefinitionId);
    }
}
