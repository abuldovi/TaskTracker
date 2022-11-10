package abuldovi.apps.bugtracker.repositories;

import abuldovi.apps.bugtracker.models.Ticket;
import abuldovi.apps.bugtracker.models.TicketChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketChangeLogRepository extends JpaRepository<TicketChangeLog, Integer> {

    List<TicketChangeLog> findAllByTicketOrderByCreatedatDesc(Ticket ticket);

}
