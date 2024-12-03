package com.zedeck.projectmanagement;
import com.zedeck.projectmanagement.dtos.DashboardDto;
import com.zedeck.projectmanagement.models.UserAccount;
import com.zedeck.projectmanagement.repositories.ProjectRepository;
import com.zedeck.projectmanagement.repositories.TaskRepository;
import com.zedeck.projectmanagement.repositories.UserAccountRepository;
import com.zedeck.projectmanagement.serviceImpl.DashboardServiceImpl;
import com.zedeck.projectmanagement.utils.Response;
import com.zedeck.projectmanagement.utils.ResponseCode;
import com.zedeck.projectmanagement.utils.userextractor.LoggedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardServiceImplTest {

    @Mock
    private LoggedUser loggedUser;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserAccountRepository accountRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private UserAccount mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new UserAccount();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
    }

    @Test
    void testGetDashboardData_UserLoggedIn() {
        when(loggedUser.getUser()).thenReturn(mockUser);

        when(projectRepository.countAllByDeletedFalse()).thenReturn(10L);
        when(taskRepository.countAllByDeletedFalse()).thenReturn(20L);
        when(accountRepository.countAllByDeletedIsFalse()).thenReturn(5L);
        when(taskRepository.countAllByStatus()).thenReturn(
                List.of(new Object[]{"0", 15L}, new Object[]{"1", 10L})
        );

        Response<DashboardDto> response = dashboardService.getDashboardData();

        assertNotNull(response);
        assertFalse(response.isError());
        assertEquals(ResponseCode.SUCCESS, response.getCode());
        assertEquals(10L, response.getData().getTotalProjects());
        assertEquals(20L, response.getData().getTotalTasks());
        assertEquals(5L, response.getData().getTotalUsers());
        assertEquals(2, response.getData().getTaskStatusCounts().size());

        verify(projectRepository, times(1)).countAllByDeletedFalse();
        verify(taskRepository, times(1)).countAllByDeletedFalse();
        verify(accountRepository, times(1)).countAllByDeletedIsFalse();
    }

    @Test
    void testGetDashboardData_UserNotLoggedIn() {
        when(loggedUser.getUser()).thenReturn(null);

        Response<DashboardDto> response = dashboardService.getDashboardData();


        assertNotNull(response);
        assertTrue(response.isError());
        assertEquals(ResponseCode.UNAUTHORIZED, response.getCode());
        assertNull(response.getData());
    }
}

