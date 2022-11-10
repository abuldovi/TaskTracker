package abuldovi.apps.bugtracker.services;

import abuldovi.apps.bugtracker.models.Project;
import abuldovi.apps.bugtracker.models.ProjectChangeLog;
import abuldovi.apps.bugtracker.repositories.ProjectChangeLogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectChangeLogService {

    private final ProjectChangeLogRepository projectChangeLogRepository;
    private final UserService userService;


    public ProjectChangeLogService(ProjectChangeLogRepository projectChangeLogRepository, UserService userService) {
        this.projectChangeLogRepository = projectChangeLogRepository;
        this.userService = userService;

    }

    public void save(int projectId, String changes, String madeby){
        ProjectChangeLog projectChangeLog = new ProjectChangeLog();
        projectChangeLog.setDescription(changes);
        projectChangeLog.setCreatedat(LocalDateTime.now());
        Project project = new Project();
        project.setId(projectId);
        projectChangeLog.setProject(project);
        projectChangeLog.setMadeby(userService.findByUsername(madeby).get());
        projectChangeLogRepository.save(projectChangeLog);
    }
    public List<ProjectChangeLog> findAllByProject(Project project){
        return projectChangeLogRepository.findAllByProjectOrderByCreatedatDesc(project);
    }
}
