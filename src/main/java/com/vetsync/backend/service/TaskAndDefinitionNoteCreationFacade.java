package com.vetsync.backend.service;

import com.vetsync.backend.domain.PatientDayTaskDefinitionNote;
import com.vetsync.backend.domain.Task;
import com.vetsync.backend.dto.patientDayTaskDefinitionNote.PatientDayTaskDefinitionNoteCreateRequest;
import com.vetsync.backend.dto.patientDayTaskDefinitionNote.PatientDayTaskDefinitionNoteInfoResponse;
import com.vetsync.backend.dto.task.TaskCreateRequest;
import com.vetsync.backend.dto.task.TaskInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskAndDefinitionNoteCreationFacade {

    private final TaskCommandService taskCommandService;
    private final PatientDayTaskDefinitionNoteService noteService;

    @Transactional
    public TaskInfoResponse createTask(
            UUID hospitalId,
            UUID staffId,
            TaskCreateRequest req
    ) {
        Task task = taskCommandService.createInternal(
                hospitalId, staffId, req
        );

        // 공용 노트가 이미 있으면 연결
        PatientDayTaskDefinitionNote note =
                noteService.findByContext(
                        hospitalId,
                        req.patientId(),
                        req.taskDate(),
                        req.taskDefinitionId()
                );

        if (note != null) {
            task.setPatientDayTaskDefinitionNote(note);
        }

        return TaskInfoResponse.from(task);
    }

    @Transactional
    public PatientDayTaskDefinitionNoteInfoResponse createPatientDayTaskDefinition(UUID hospitalId, UUID patientId, LocalDate taskDate, PatientDayTaskDefinitionNoteCreateRequest request){
        PatientDayTaskDefinitionNote note = noteService.createDefinitionNote(hospitalId, patientId, taskDate, request);
        taskCommandService.linkTasksToDefinitionNote(hospitalId, patientId, taskDate, request.taskDefinitionId(), note);
        return PatientDayTaskDefinitionNoteInfoResponse.from(note);
    }
}
