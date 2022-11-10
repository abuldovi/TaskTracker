package abuldovi.apps.bugtracker.repositories;

import abuldovi.apps.bugtracker.models.Project;
import abuldovi.apps.bugtracker.models.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findUserByUsername(String username);

}
