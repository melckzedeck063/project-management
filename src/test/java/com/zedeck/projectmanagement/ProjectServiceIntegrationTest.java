package com.zedeck.projectmanagement;

import com.zedeck.projectmanagement.dtos.ProjectDto;
import com.zedeck.projectmanagement.models.Project;
import com.zedeck.projectmanagement.repositories.ProjectRepository;
import com.zedeck.projectmanagement.service.ProjectService;
import com.zedeck.projectmanagement.utils.Response;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.testng.AssertJUnit.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProjectServiceIntegrationTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @Transactional
    void testCreateProject_Integration() {

        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Integration Project");
        projectDto.setDescription("Integration Test Description");
        projectDto.setStart_date("2024-12-01");
        projectDto.setEnd_date("2025-12-01");

        Response<Project> response = projectService.createProject(projectDto);

        assertFalse(response.isError());
        assertNotNull(response.getData());
        assertEquals("Integration Project", response.getData().getName());
        assertEquals("Integration Test Description", response.getData().getDescription());

        projectRepository.deleteAll();
    }
}

