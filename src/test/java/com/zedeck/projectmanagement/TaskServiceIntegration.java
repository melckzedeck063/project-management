package com.zedeck.projectmanagement;


import com.zedeck.projectmanagement.dtos.TaskDto;
import com.zedeck.projectmanagement.models.Project;
import com.zedeck.projectmanagement.models.Task;
import com.zedeck.projectmanagement.repositories.ProjectRepository;
import com.zedeck.projectmanagement.repositories.TaskRepository;
import com.zedeck.projectmanagement.repositories.UserAccountRepository;
import com.zedeck.projectmanagement.service.TaskService;
import com.zedeck.projectmanagement.utils.Response;
import com.zedeck.projectmanagement.utils.ResponseCode;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.testng.AssertJUnit.*;

@SpringBootTest
@Transactional
class TaskServiceIntegrationTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private TaskService taskService;

    @Test
    void testAddTaskIntegration() {
        // Arrange
        Project project = new Project();
        project.setName("Test Project");
        projectRepository.save(project);

        TaskDto taskDto = new TaskDto("Integration Task", "Integration Description", null, project.getId(), "0L");
        Response<Task> response = taskService.addTask(project.getId(), taskDto);

        assertNotNull(response);
        assertFalse(response.isError());
        assertEquals(ResponseCode.SUCCESS, response.getCode());
        assertNotNull(response.getData());
    }
}

