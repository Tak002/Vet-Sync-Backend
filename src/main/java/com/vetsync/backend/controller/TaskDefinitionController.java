package com.vetsync.backend.controller;

import com.vetsync.backend.dto.task.TaskDefinitionResponse;
import com.vetsync.backend.global.annotation.HospitalId;
import com.vetsync.backend.service.TaskDefinitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "TaskDefinition", description = "업무 정의 조회/관리 API")
@RestController
@RequestMapping("/task-definitions")
@RequiredArgsConstructor
public class TaskDefinitionController {
    private final TaskDefinitionService taskDefinitionService;


    @GetMapping
    @Operation(
            summary = "병원의 업무 정의 목록 ",
            description = """
                    해당 병원에서 사용 가능한 업무 정의(TaskDefinition) 목록을 조회합니다.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TaskDefinitionResponse.class)),
                                    examples = @ExampleObject(
                                            name = "success",
                                            value = """
                                                    [
                                                      {
                                                        "id": "11111111-1111-1111-1111-111111111111",
                                                        "name": "체중",
                                                        "fixed": true,
                                                        "description": "환자 체중 측정"
                                                      },
                                                      {
                                                        "id": "22222222-2222-2222-2222-222222222222",
                                                        "name": "심장약",
                                                        "fixed": false,
                                                        "description": "병원 지정 약"
                                                      }
                                                    ]
                                                    """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<List<TaskDefinitionResponse>> getAllTaskDefinitions(@HospitalId UUID hospitalId) {
        return ResponseEntity.ok(taskDefinitionService.getAccessibleTaskDefinitions(hospitalId));
    }
}
