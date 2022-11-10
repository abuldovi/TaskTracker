package abuldovi.apps.bugtracker.repositories;

import abuldovi.apps.bugtracker.models.Project;
import abuldovi.apps.bugtracker.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectsRepository extends JpaRepository<Project, Integer> {

    Integer countAllByUserEntitiesContains(UserEntity userEntity);
}
