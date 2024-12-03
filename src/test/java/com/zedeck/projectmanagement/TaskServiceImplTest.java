package com.zedeck.projectmanagement;

import com.zedeck.projectmanagement.dtos.EmailDto;
import com.zedeck.projectmanagement.dtos.TaskDto;
import com.zedeck.projectmanagement.models.Project;
import com.zedeck.projectmanagement.models.Task;
import com.zedeck.projectmanagement.models.UserAccount;
import com.zedeck.projectmanagement.repositories.ProjectRepository;
import com.zedeck.projectmanagement.repositories.TaskRepository;
import com.zedeck.projectmanagement.repositories.UserAccountRepository;
import com.zedeck.projectmanagement.service.EmailService;
import com.zedeck.projectmanagement.serviceImpl.TaskServiceImpl;
import com.zedeck.projectmanagement.utils.Response;
import com.zedeck.projectmanagement.utils.ResponseCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.testng.AssertJUnit.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Test
    void testAddTask_Success() {
        Long projectId = 1L;
        TaskDto taskDto = new TaskDto("Test Task", "Description", null, 0L, "0L");
        Task task = new Task();
        Project project = new Project();
        project.setId(projectId);

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        Response<Task> response = taskService.addTask(projectId, taskDto);

        assertNotNull(response);
        assertFalse(response.isError());
        assertEquals(ResponseCode.SUCCESS, response.getCode());
        Mockito.verify(taskRepository, Mockito.times(1)).save(Mockito.any(Task.class));
    }

    @Test
    void testAssignTask_Success() {

        Long taskId = 1L;
        Long userId = 1L;
        Task task = new Task();
        UserAccount user = new UserAccount();
        user.setId(userId);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Mockito.when(userAccountRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        Response<Task> response = taskService.assignTask(taskId, userId);
        assertNotNull(response);
        assertFalse(response.isError());
        assertEquals(ResponseCode.SUCCESS, response.getCode());
        Mockito.verify(taskRepository, Mockito.times(1)).save(task);
        Mockito.verify(emailService, Mockito.times(1)).sendEmail(Mockito.any(EmailDto.class));
    }

}

