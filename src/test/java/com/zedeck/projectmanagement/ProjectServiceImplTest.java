package com.zedeck.projectmanagement;


import com.zedeck.projectmanagement.dtos.ProjectDto;
import com.zedeck.projectmanagement.models.Project;
import com.zedeck.projectmanagement.models.UserAccount;
import com.zedeck.projectmanagement.repositories.ProjectRepository;
import com.zedeck.projectmanagement.repositories.UserAccountRepository;
import com.zedeck.projectmanagement.serviceImpl.ProjectServiceImpl;
import com.zedeck.projectmanagement.utils.Response;
import com.zedeck.projectmanagement.utils.ResponseCode;
import com.zedeck.projectmanagement.utils.userextractor.LoggedUser;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.annotations.Test;

import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.testng.AssertJUnit.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

    @Mock
    private LoggedUser loggedUser;

    @Mock
    private UserAccountRepository accountRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void testCreateProject_Success() {
        // Arrange
        UserAccount user = new UserAccount();
        user.setId(1L);

        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Project A");
        projectDto.setDescription("Description A");
        projectDto.setStart_date("2024-12-01");
        projectDto.setEnd_date("2025-12-01");

        when(loggedUser.getUser()).thenReturn(user);
        when(projectRepository.save(Mockito.any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Response<Project> response = projectService.createProject(projectDto);

        // Assert
        assertFalse(response.isError());
        assertEquals("Project created successfully", response.getMessage());
        Mockito.verify(projectRepository, Mockito.times(1)).save(Mockito.any(Project.class));
    }

    @Test
    void testCreateProject_Unauthorized() {
        // Arrange
        when(loggedUser.getUser()).thenReturn(null);

        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Project A");

        // Act
        Response<Project> response = projectService.createProject(projectDto);

        // Assert
        assertTrue(response.isError());
        assertEquals(ResponseCode.UNAUTHORIZED, response.getCode());
        Mockito.verify(projectRepository, never()).save(Mockito.any(Project.class));
    }

    @Test
    void testUpdateProject_Success() {
        // Arrange
        Long projectId = 1L;
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Old Project");

        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("Updated Project");
        projectDto.setDescription("Updated Description");

        when(projectRepository.findFirstById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(Mockito.any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Response<Project> response = projectService.updateProject(projectId, projectDto);

        // Assert
        assertFalse(response.isError());
        assertEquals("Updated Project", response.getData().getName());
        assertEquals("Updated Description", response.getData().getDescription());
        Mockito.verify(projectRepository, times(1)).save(Mockito.any(Project.class));
    }

}

