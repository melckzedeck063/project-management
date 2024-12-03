package com.zedeck.projectmanagement.service;

import com.zedeck.projectmanagement.dtos.TaskDto;
import com.zedeck.projectmanagement.models.Task;
import com.zedeck.projectmanagement.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    Response<Task> addTask(Long projectId,TaskDto taskDto);

    Response<Task> updateTask(Long taskId,TaskDto taskDto);

    Response<Task> assignTask(Long taskId,Long userId);

    Response<Task> markTaskAsComplete(Long taskId);

    Response<Task> deleteTask(Long taskId);

    List<Task> filterTasks(String name, String status);

    Page<Task> getTaskByProjectId(Long projectId, Pageable pageable);
}
