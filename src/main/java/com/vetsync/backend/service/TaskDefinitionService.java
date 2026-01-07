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
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskDefinitionService {
    private final TaskDefinitionRepository taskDefinitionRepository;
    private final HospitalRepository hospitalRepository;
    private final EntityManager entityManager;

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
        return taskDefinitionRepository.findAllByHospital_IdOrderByIsFixedDescOrderAscNameAsc(hospitalId).stream()
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

        // order 처리: fixed=false면 무시/NULL, fixed=true면 삽입 방식
        if (!request.fixed()) {
            if (request.order() != null) {
                throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "fixed=false인 항목에는 order를 지정할 수 없습니다.");
            }
            entity.setOrder(null);
        } else {
            int max = taskDefinitionRepository.getMaxOrder(hospitalId);
            Integer desired = request.order();
            if (desired == null || desired < 1) {
                desired = max + 1; // append
            } else if (desired > max + 1) {
                desired = max + 1; // 범위를 넘어가면 맨뒤로
            }

            if (desired <= max) {
                // 1) desired..max 구간을 내림차순으로 한 칸씩 우측 시프트
                var range = taskDefinitionRepository.findRangeDesc(hospitalId, desired, max);
                for (TaskDefinition td : range) {
                    td.setOrder(td.getOrder() + 1);
                }
                entityManager.flush();
            }
            // 2) 신규 엔티티 최종 위치 설정
            entity.setOrder(desired);
        }

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

        boolean beforeFixed = entity.isFixed();
        Integer beforeOrder = entity.getOrder();

        entity.setName(normalizedName);
        entity.setFixed(request.fixed());
        entity.setDescription(request.description());
        entity.setOptions(request.options());

        boolean afterFixed = request.fixed();
        Integer desiredOrder = request.order();

        if (!afterFixed) {
            // fixed=false로 저장하는 경우: order는 없어야 함
            if (desiredOrder != null) {
                throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "fixed=false인 항목에는 order를 지정할 수 없습니다.");
            }
            // true->false 로 토글되면 좌측 시프트
            if (beforeFixed && beforeOrder != null) {
                // 순차 업데이트로 변경: (beforeOrder 보다 큰) 항목들을 order-1 하도록 개별 적용
                var range = taskDefinitionRepository.findRangeAscExcludeSelf(hospitalId, beforeOrder + 1, taskDefinitionRepository.getMaxOrder(hospitalId), taskDefinitionId);
                for (TaskDefinition td : range) {
                    td.setOrder(td.getOrder() - 1);
                }
                entityManager.flush();
            }
            entity.setOrder(null);
        } else {
            // fixed=true 인 상태
            int max = taskDefinitionRepository.getMaxOrder(hospitalId);
            if (!beforeFixed) {
                // false -> true 로 토글되는 케이스: 삽입 (순차 업데이트 + 단계별 flush)
                if (desiredOrder == null || desiredOrder < 1) {
                    desiredOrder = max + 1;
                } else if (desiredOrder > max + 1) {
                    desiredOrder = max + 1;
                }

                // 1) 대상 엔티티 order 를 우선 null로 설정하여 충돌 슬롯 제거
                entity.setOrder(null);
                entityManager.flush();

                // 2) desired..max 구간을 내림차순으로 한 칸씩 우측 시프트
                if (desiredOrder <= max) {
                    var range = taskDefinitionRepository.findRangeDesc(hospitalId, desiredOrder, max);
                    for (TaskDefinition td : range) {
                        td.setOrder(td.getOrder() + 1);
                    }
                    entityManager.flush();
                }
                // 3) 대상 엔티티 최종 위치 설정
                entity.setOrder(desiredOrder);
                entityManager.flush();
            } else {
                // true -> true: 순서 변경 여부 확인
                if (beforeOrder == null) beforeOrder = max + 1; // 안전장치
                if (desiredOrder == null || desiredOrder < 1) {
                    desiredOrder = beforeOrder; // 변경 의사 없음으로 간주
                } else if (desiredOrder > max) {
                    desiredOrder = max; // 맨뒤로 이동 요청 시 max로 보정
                }

                if (!beforeOrder.equals(desiredOrder)) {
                    // 부분 유니크 인덱스(hospital_id, order_no where is_fixed=true) 충돌을 피하기 위해
                    // 범위 일괄 업데이트 대신 개별 순차 업데이트로 처리한다.

                    // 1) 이동 대상의 order를 일시적으로 null로 비워 충돌 슬롯 제거
                    entity.setOrder(null);
                    entityManager.flush();

                    if (beforeOrder < desiredOrder) {
                        // X < Y: (X < o <= Y) 각각 o-1로 조정 (오름차순 진행)
                        var range = taskDefinitionRepository.findRangeAscExcludeSelf(hospitalId, beforeOrder + 1, desiredOrder, taskDefinitionId);
                        for (TaskDefinition td : range) {
                            td.setOrder(td.getOrder() - 1);
                        }
                        entityManager.flush();
                    } else {
                        // X > Y: (Y <= o < X) 각각 o+1로 조정 (내림차순 진행)
                        var range = taskDefinitionRepository.findRangeDescExcludeSelf(hospitalId, desiredOrder, beforeOrder - 1, taskDefinitionId);
                        for (TaskDefinition td : range) {
                            td.setOrder(td.getOrder() + 1);
                        }
                        entityManager.flush();
                    }

                    // 3) 대상 엔티티 최종 위치 설정
                    entity.setOrder(desiredOrder);
                    entityManager.flush();
                }
            }
        }

        return TaskDefinitionResponse.from(entity);
    }

    @Transactional
    public void delete(UUID hospitalId, UUID taskDefinitionId) {
        validateTaskDefinitionAccessible(hospitalId, taskDefinitionId);
        TaskDefinition entity = taskDefinitionRepository.findById(taskDefinitionId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_DEFINITION_NOT_FOUND));
        Integer beforeOrder = entity.getOrder();
        boolean wasFixed = entity.isFixed();

        taskDefinitionRepository.deleteById(taskDefinitionId);

        if (wasFixed && beforeOrder != null) {
            int max = taskDefinitionRepository.getMaxOrder(hospitalId);
            if (beforeOrder < max) {
                var range = taskDefinitionRepository.findRangeAsc(hospitalId, beforeOrder + 1, max);
                for (TaskDefinition td : range) {
                    td.setOrder(td.getOrder() - 1);
                }
                entityManager.flush();
            }
        }
    }
}
