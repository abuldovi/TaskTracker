package abuldovi.apps.bugtracker.repositories;


import abuldovi.apps.bugtracker.models.Project;
import abuldovi.apps.bugtracker.models.ProjectChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectChangeLogRepository extends JpaRepository<ProjectChangeLog, Integer> {
    List<ProjectChangeLog> findAllByProjectOrderByCreatedatDesc(Project project);
}
