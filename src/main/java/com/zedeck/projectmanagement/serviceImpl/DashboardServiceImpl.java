package com.zedeck.projectmanagement.serviceImpl;

import com.zedeck.projectmanagement.dtos.DashboardDto;
import com.zedeck.projectmanagement.models.UserAccount;
import com.zedeck.projectmanagement.repositories.ProjectRepository;
import com.zedeck.projectmanagement.repositories.TaskRepository;
import com.zedeck.projectmanagement.repositories.UserAccountRepository;
import com.zedeck.projectmanagement.service.DashboardService;
import com.zedeck.projectmanagement.utils.Response;
import com.zedeck.projectmanagement.utils.ResponseCode;
import com.zedeck.projectmanagement.utils.userextractor.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private LoggedUser loggedUser;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserAccountRepository accountRepository;

    private static final Map<String, String> STATUS_MAP = Map.of(
            "0", "PENDING",
            "1", "IN_PROGRESS",
            "2", "COMPLETED"
    );

    @Override
    @Cacheable("dashboardData")
    public Response<DashboardDto> getDashboardData() {
        try {
            UserAccount user = loggedUser.getUser();
            if (user == null) {
                return new Response<>(true, ResponseCode.UNAUTHORIZED, "You are not logged in");
            }

            Long totalProjects = projectRepository.countAllByDeletedFalse();
            Long totalTasks = taskRepository.countAllByDeletedFalse();
            Long totalUsers = accountRepository.countAllByDeletedIsFalse();

            DashboardDto dashboardDto = new DashboardDto();
            dashboardDto.setTotalProjects(totalProjects);
            dashboardDto.setTotalTasks(totalTasks);
            dashboardDto.setTotalUsers(totalUsers);

            List<Object[]> rawResults = taskRepository.countAllByStatus();
            List<DashboardDto.TaskStatusCount> groupedTasks = rawResults.stream()
                    .map(row -> {
                        Long count = ((Number) row[1]).longValue();
                        String numericStatus = row[0].toString();
                        String readableStatus = STATUS_MAP.getOrDefault(numericStatus, "Pending");
                        return new DashboardDto.TaskStatusCount(count, readableStatus);
                    })
                    .toList();

            dashboardDto.setTaskStatusCounts(groupedTasks);
            return new Response<>(false, ResponseCode.SUCCESS, dashboardDto, "Data found successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(true, ResponseCode.FAIL, "An error occurred while fetching dashboard data");
        }
    }
}
