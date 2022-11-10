package abuldovi.apps.bugtracker.services;

import abuldovi.apps.bugtracker.models.Ticket;
import abuldovi.apps.bugtracker.models.TicketChangeLog;
import abuldovi.apps.bugtracker.repositories.TicketChangeLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketChangeLogService {

    private final UserService userService;
    private final TicketChangeLogRepository ticketChangeLogRepository;

    public TicketChangeLogService(UserService userService, TicketChangeLogRepository ticketChangeLogRepository) {
        this.userService = userService;
        this.ticketChangeLogRepository = ticketChangeLogRepository;
    }


    public void save(int ticketId, String changes, String madeby){
        TicketChangeLog ticketChangeLog = new TicketChangeLog();
        ticketChangeLog.setDescription(changes);
        ticketChangeLog.setCreatedat(LocalDateTime.now());
        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticketChangeLog.setTicket(ticket);
        ticketChangeLog.setMadeby(userService.findByUsername(madeby).get());
        ticketChangeLogRepository.save(ticketChangeLog);
    }
    public List<TicketChangeLog> findAllByTicket(Ticket ticket){
        return ticketChangeLogRepository.findAllByTicketOrderByCreatedatDesc(ticket);
    }


}
