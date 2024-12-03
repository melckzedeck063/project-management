package com.zedeck.projectmanagement.controllers;

import com.zedeck.projectmanagement.dtos.TaskDto;
import com.zedeck.projectmanagement.models.Task;
import com.zedeck.projectmanagement.service.TaskService;
import com.zedeck.projectmanagement.utils.Response;
import com.zedeck.projectmanagement.utils.ResponseCode;
import com.zedeck.projectmanagement.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/projects/{id}/tasks")
    @PreAuthorize("hasAuthority('manager:create')")
    public ResponseEntity<?> addTask( @PathVariable(value = "id") Long projectId, @RequestBody TaskDto taskDto) {
        Response<Task> response =  taskService.addTask(projectId,taskDto);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/projects/{id}/tasks")
    public ResponseEntity<?> getAllProjectTasks(@PathVariable(value = "id") Long projectId, @RequestParam(value = "page", defaultValue = "0")Integer page,
                                                @RequestParam(value = "size", defaultValue = "20")Integer size) {
        PageRequest pageRequest =  PageRequest.of(page,size);
        Page<Task> taskPage = taskService.getTaskByProjectId(projectId,pageRequest);
        return ResponseEntity.ok().body(taskPage);
    }

    @PutMapping("/tasks/{id}")
    @PreAuthorize("hasAnyAuthority('manager:update')")
    public ResponseEntity<?> updateTask(@PathVariable(value = "id") Long projectId, @RequestBody TaskDto taskDto) {
        Response<Task> response = taskService.updateTask(projectId,taskDto);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/tasks/{id}/assign")
    public ResponseEntity<?> assignTask(@PathVariable(value = "id") Long projectId, @RequestBody TaskDto taskDto) {
        Response<Task> response = taskService.assignTask(projectId, taskDto.getAssignee_id());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/tasks/{id}/status")
    public ResponseEntity<?> completeTask(@PathVariable(value = "id") Long projectId) {
        Response<Task> response = taskService.markTaskAsComplete(projectId);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable(value = "id") Long projectId) {
        Response<Task> response = taskService.deleteTask(projectId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/tasks/filter")
    public ResponseEntity<?> searchTask(@RequestParam String name, @RequestParam String status) {
        List<Task> response =  taskService.filterTasks(name, status);
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tasks found matching the criteria.");
        }
        return ResponseEntity.ok().body(response);
    }


}

