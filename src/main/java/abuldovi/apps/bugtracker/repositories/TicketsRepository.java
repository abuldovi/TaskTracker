package abuldovi.apps.bugtracker.repositories;

import abuldovi.apps.bugtracker.models.Ticket;
import abuldovi.apps.bugtracker.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketsRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findAllByProjectId(int id);
    List<Ticket> findAllByUserEntitiesContains(UserEntity userEntity);
    Optional<Ticket> findTicketByIdAndProjectId(int id, int projectId);
    Optional<Ticket> findTicketByNameAndProjectId(String name, int projectId);

    Integer countAllByUserEntitiesContains(UserEntity userEntity);
}
