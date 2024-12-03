package com.zedeck.projectmanagement.controllers;


import com.zedeck.projectmanagement.dtos.ProjectDto;
import com.zedeck.projectmanagement.models.Project;
import com.zedeck.projectmanagement.service.ProjectService;
import com.zedeck.projectmanagement.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/projects")
    @PreAuthorize("hasAuthority('admin:create') or hasAuthority('manager:create')")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) {
        Response<Project> response =  projectService.createProject(projectDto);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/projects")
    public ResponseEntity<?> getAllProjects(@RequestParam(name = "page", defaultValue = "0")Integer page,
                                            @RequestParam(name = "size", defaultValue =  "20")Integer size) {
        PageRequest pageRequest =  PageRequest.of(page,size);
        Page<Project> projects = projectService.getAllProjects(pageRequest);

        return ResponseEntity.ok().body(projects);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        Response<Project> response = projectService.getProjectById(id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/projects/{id}")
    @PreAuthorize("hasAuthority('admin:update') or hasAuthority('manager:update')")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody ProjectDto projectDto) {
        Response<Project> response =  projectService.updateProject(id, projectDto);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/projects/{id}")
    @PreAuthorize("hasAuthority('admin:delete') or hasAuthority('manager:delete')")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        Response<Project> response =  projectService.deleteProject(id);

        return ResponseEntity.ok().body(response);
    }

}
