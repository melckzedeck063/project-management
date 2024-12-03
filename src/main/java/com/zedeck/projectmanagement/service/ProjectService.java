package com.zedeck.projectmanagement.service;

import com.zedeck.projectmanagement.dtos.ProjectDto;
import com.zedeck.projectmanagement.models.Project;
import com.zedeck.projectmanagement.utils.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    Response<Project> createProject(ProjectDto projectDto);

    Response<Project> updateProject(Long id, ProjectDto projectDto);
    Page<Project> getAllProjects(Pageable pageable);
    Response<Project> getProjectById(Long id);
    Response<Project> deleteProject(Long id);
}
