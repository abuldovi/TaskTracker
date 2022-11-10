package abuldovi.apps.bugtracker.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket_changelog")
@Data
public class TicketChangeLog{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    int id;
    @ManyToOne
    @JoinColumn(name="ticket_id", nullable=false)
    Ticket ticket;

    String description;

    LocalDateTime createdat;

    @ManyToOne
    @JoinColumn(name="madeby_id", nullable=false)
    UserEntity madeby;


}

