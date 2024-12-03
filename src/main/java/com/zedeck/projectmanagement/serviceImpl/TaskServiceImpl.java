package com.zedeck.projectmanagement.serviceImpl;

import com.zedeck.projectmanagement.dtos.EmailDto;
import com.zedeck.projectmanagement.dtos.TaskDto;
import com.zedeck.projectmanagement.models.Project;
import com.zedeck.projectmanagement.models.Task;
import com.zedeck.projectmanagement.models.UserAccount;
import com.zedeck.projectmanagement.repositories.ProjectRepository;
import com.zedeck.projectmanagement.repositories.TaskRepository;
import com.zedeck.projectmanagement.repositories.UserAccountRepository;
import com.zedeck.projectmanagement.service.EmailService;
import com.zedeck.projectmanagement.service.TaskService;
import com.zedeck.projectmanagement.utils.Response;
import com.zedeck.projectmanagement.utils.ResponseCode;
import com.zedeck.projectmanagement.utils.Status;
import com.zedeck.projectmanagement.utils.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private EmailService emailService;



    @Override
    public Response<Task> addTask(Long projectId,TaskDto taskDto) {
        try {
            Task task = new Task();

            if(taskDto.getName() == null || taskDto.getName().isEmpty()) {
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"Task name cannot be empty");
            }
            if(taskDto.getDescription() == null || taskDto.getDescription().isEmpty()) {
                return new Response<>(true,ResponseCode.NULL_ARGUMENT,"Task description cannot be empty");
            }

            if(!taskDto.getName().isBlank() && !Objects.equals(taskDto.getName(), task.getName())){
                task.setName(taskDto.getName());
            }
            if(!taskDto.getDescription().isBlank() && !Objects.equals(taskDto.getDescription(), task.getDescription())){
                task.setDescription(taskDto.getDescription());
            }

            task.setStatus(Status.Pending);

            if(projectId != null || projectId.equals("")){
                Optional<Project> project = projectRepository.findById(projectId);
                if(project.isEmpty()){
                    throw new ResourceNotFoundException("Project not found");
                }
                project.ifPresent(task::setProject_id);

            }
            else
                throw new IllegalArgumentException("Project id cannot be empty");

            Task task1 =  taskRepository.save(task);

            return new Response<>(false,ResponseCode.SUCCESS,task1,"Task added successfully");

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response<Task> updateTask(Long id, TaskDto taskDto) {
        try {
            Optional<Task> taskOptional =  taskRepository.findById(id);
            if(taskOptional.isPresent()) {
                Task task = taskOptional.get();

                if(!taskDto.getName().isBlank() && !Objects.equals(taskDto.getName(), task.getName())){
                    task.setName(taskDto.getName());
                }
                if(!taskDto.getDescription().isBlank() && !Objects.equals(taskDto.getDescription(), task.getDescription())){
                    task.setDescription(taskDto.getDescription());
                }
                if(!taskDto.getStatus().isBlank() && !Objects.equals(taskDto.getStatus(), task.getStatus())){
                    if(taskDto.getStatus().equals(Status.Pending)){
                        task.setStatus(Status.Pending);
                    } else if (taskDto.getStatus().equals(Status.Completed)) {
                        task.setStatus(Status.Completed);
                    }
                    else if(!taskDto.getStatus().equals(Status.In_Progress)){
                        task.setStatus(Status.In_Progress);
                    }
                    task.setStatus(Status.Pending);
                }
                if(taskDto.getProject_id() != 0) {
                    Optional<Project> project = projectRepository.findById(taskDto.getProject_id());
                    project.ifPresent(task::setProject_id);
                }
                if(taskDto.getAssignee_id() != 0){
                    Optional<UserAccount> accountOptional = userAccountRepository.findById(taskDto.getAssignee_id());
                    accountOptional.ifPresent(task::setAssignee_id);
                }

                Task task1 =  taskRepository.save(task);
                return new Response<>(false,ResponseCode.SUCCESS,task1,"Task updated successfully");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Response<Task> assignTask(Long taskId, Long userId) {
        try {
            Optional<Task> taskOptional =  taskRepository.findById(taskId);
            if(taskOptional.isPresent()) {
                Task task = taskOptional.get();

                Optional<UserAccount> userAccountOptional = userAccountRepository.findById(userId);
                userAccountOptional.ifPresent(task::setAssignee_id);

                Task task1 =  taskRepository.save(task);

                emailService.sendEmail(new EmailDto("melckzedeck063@gmail.com", "Task notification","Task assigned successfully"));
                return new Response<>(false,ResponseCode.SUCCESS,task1,"Task assigned successfully");
            }
            else {
                throw new ResourceNotFoundException("Task not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Task> markTaskAsComplete(Long taskId) {
        try {
            Optional<Task> optionalTask =  taskRepository.findById(taskId);
            if(optionalTask.isPresent()) {
                Task task = optionalTask.get();

                task.setStatus(Status.Completed);
                Task task1 =  taskRepository.save(task);

                emailService.sendEmail(new EmailDto("melckzedeck063@gmail.com", "Task notification","Task marked as completed"));
                return new Response<>(false,ResponseCode.SUCCESS,task1,"Task completed successfully");
            }

            else {
                throw new ResourceNotFoundException("Task not found");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Response<Task> deleteTask(Long taskId) {
        try {
            Optional<Task> task = taskRepository.findById(taskId);
            if (task.isPresent()) {
                Task task1 = task.get();

                task1.setDeleted(true);
                taskRepository.save(task.get());
                return new Response<>(false, ResponseCode.SUCCESS, "Task deleted successfully");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Task> filterTasks(String name, String status) {
        try {
            Status enumStatus = null;
            if ("completed".equalsIgnoreCase(status.toString())) {
                enumStatus = Status.Completed;
            } else if ("in-progress".equalsIgnoreCase(status.toString())) {
                enumStatus = Status.In_Progress;
            } else if ("pending".equalsIgnoreCase(status.toString())) {
                enumStatus = Status.Pending;
            }

            assert enumStatus != null;
            return taskRepository.findByNameOrStatus(name,enumStatus.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    @Override
    public Page<Task> getTaskByProjectId(Long id, Pageable pageable) {
        try {
            Optional<Project> project = projectRepository.findById(id);
            if (project.isPresent()) {
                Page<Task> tasks = taskRepository.findAllByProject_id(id, pageable);
                return tasks;
            }
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
