package com.zedeck.projectmanagement.serviceImpl;
import com.zedeck.projectmanagement.dtos.ProjectDto;
import com.zedeck.projectmanagement.models.Project;
import com.zedeck.projectmanagement.models.UserAccount;
import com.zedeck.projectmanagement.repositories.ProjectRepository;
import com.zedeck.projectmanagement.repositories.UserAccountRepository;
import com.zedeck.projectmanagement.service.ProjectService;
import com.zedeck.projectmanagement.utils.Response;
import com.zedeck.projectmanagement.utils.ResponseCode;
import com.zedeck.projectmanagement.utils.userextractor.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private LoggedUser loggedUser;

    @Autowired
    private UserAccountRepository accountRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Response<Project> createProject(ProjectDto projectDto) {
        try {
            UserAccount user = loggedUser.getUser();

            Project project = new Project();

            if (user == null) {
                return new Response<>(true, ResponseCode.UNAUTHORIZED, "Unauthorized");
            }
            if (projectDto.getName() == null || projectDto.getName().trim().equals("")) {
                return new Response<>(true, ResponseCode.NULL_ARGUMENT, "Name cannot be empty");
            }
            if (projectDto.getDescription() == null || projectDto.getDescription().trim().equals("")) {
                return new Response<>(true, ResponseCode.NULL_ARGUMENT, "Description cannot be empty");
            }
            if (projectDto.getStart_date() == null || projectDto.getStart_date().equals("")) {
                return new Response<>(true, ResponseCode.NULL_ARGUMENT, "Start date cannot be empty");
            }
            if (projectDto.getEnd_date() == null || projectDto.getEnd_date().equals("")) {
                return new Response<>(true, ResponseCode.NULL_ARGUMENT, "End date cannot be empty");
            }
            if (!projectDto.getName().isBlank() && !Objects.equals(projectDto.getName(), project.getName())) {
                project.setName(projectDto.getName());
            }
            if (!projectDto.getDescription().isBlank() && !Objects.equals(projectDto.getDescription(), project.getDescription())) {
                project.setDescription(projectDto.getDescription());
            }
            if (!projectDto.getStart_date().isBlank() && !Objects.equals(projectDto.getStart_date(), project.getStart_date())) {
                project.setStart_date(projectDto.getStart_date());
            }
            if (!projectDto.getEnd_date().isBlank() && !Objects.equals(projectDto.getEnd_date(), project.getEnd_date())) {
                project.setEnd_date(projectDto.getEnd_date());
            }

            Project project1 = projectRepository.save(project);
            return new Response<>(false, ResponseCode.SUCCESS, project1, "Project created successfully");
        }
        catch (Exception e){
            e.printStackTrace();
        }
            return new Response<>(true,ResponseCode.UNAUTHORIZED,"Operation failed");
    }

    @Override
    public Response<Project> updateProject(Long id, ProjectDto projectDto) {
        try {
            Optional<Project> projectOptional = projectRepository.findFirstById(id);

            if(projectOptional.isPresent()){
                Project project = projectOptional.get();

                if (!projectDto.getName().isBlank() && !Objects.equals(projectDto.getName(), project.getName())) {
                    project.setName(projectDto.getName());
                }
                if (!projectDto.getDescription().isBlank() && !Objects.equals(projectDto.getDescription(), project.getDescription())) {
                    project.setDescription(projectDto.getDescription());
                }
                if (!projectDto.getStart_date().isBlank() && !Objects.equals(projectDto.getStart_date(), project.getStart_date())) {
                    project.setStart_date(projectDto.getStart_date());
                }
                if (!projectDto.getEnd_date().isBlank() && !Objects.equals(projectDto.getEnd_date(), project.getEnd_date())) {
                    project.setEnd_date(projectDto.getEnd_date());
                }
                if(projectDto.getManager_id() != null){
                    Optional<UserAccount> manager =  accountRepository.findById(projectDto.getManager_id());
                    manager.ifPresent(project::setManager_id);
                }

                Project updatedProject = projectRepository.save(project);
                return new Response<>(false, ResponseCode.SUCCESS, updatedProject, "Project updated successfully");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response<>(true,ResponseCode.FAIL,"Operation failed");
    }

    @Override
    public Page<Project> getAllProjects(Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByDeletedFalse(pageable);
        return projects;
    }

    @Override
    public Response<Project> getProjectById(Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        return projectOptional.map(project -> new Response<>(false, ResponseCode.SUCCESS, project, "Project found")).orElseGet(() -> new Response<>(true, ResponseCode.FAIL, "Project not found"));
    }

    @Override
    public Response<Project> deleteProject(Long id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (projectOptional.isPresent()) {
            Project project = projectOptional.get();
            project.setDeleted(true);
            projectRepository.save(project);
            return new Response<>(true,ResponseCode.SUCCESS,"Project deleted successfully");
        }
        return new Response<>(true,ResponseCode.FAIL,"Project not found");
    }
}
