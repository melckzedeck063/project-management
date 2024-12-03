package com.zedeck.projectmanagement.service;

import com.zedeck.projectmanagement.dtos.DashboardDto;
import com.zedeck.projectmanagement.utils.Response;

public interface DashboardService {

    Response<DashboardDto> getDashboardData();
}
