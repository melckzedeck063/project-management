package com.zedeck.projectmanagement.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DashboardDto {

    private Long totalProjects;
    private Long totalUsers;
    private Long totalTasks;
    private List<TaskStatusCount> taskStatusCounts;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class TaskStatusCount {
        private Long count;
        private String status;
    }
}
