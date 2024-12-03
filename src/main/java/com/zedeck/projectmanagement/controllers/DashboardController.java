package com.zedeck.projectmanagement.controllers;

import com.zedeck.projectmanagement.dtos.DashboardDto;
import com.zedeck.projectmanagement.service.DashboardService;
import com.zedeck.projectmanagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(){
        Response<DashboardDto> response = dashboardService.getDashboardData();

        return ResponseEntity.ok().body(response);
    }
}
