package com.zedeck.projectmanagement;
import com.zedeck.projectmanagement.dtos.DashboardDto;
import com.zedeck.projectmanagement.serviceImpl.DashboardServiceImpl;
import com.zedeck.projectmanagement.utils.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DashboardServiceIntegrationTest {

    @Autowired
    private DashboardServiceImpl dashboardService;

    @Test
    void testGetDashboardData() {
        Response<DashboardDto> response = dashboardService.getDashboardData();

        assertNotNull(response);
    }
}

