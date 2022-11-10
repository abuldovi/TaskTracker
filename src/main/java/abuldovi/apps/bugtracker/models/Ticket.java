package abuldovi.apps.bugtracker.models;

import abuldovi.apps.bugtracker.states.StatusState;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tickets")
@Data
public class Ticket {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    int id;

    @NotNull
    @Size(min = 1, max = 20, message = "Name must not be between 1 and 20 characters")
    String name;

    @NotNull(message = "Status cannot be null")
    StatusState status;

    @Size(min = 1, max = 1000, message = "Description must not be empty and must be less than 1000 characters")
    String description;

    LocalDateTime createdat;
    LocalDateTime changedat;

    @ManyToOne
    UserEntity createdby;


    @ManyToOne
    @JoinColumn(name="project_id", nullable=false)
    Project project;

    @ManyToMany
    @JoinTable(name = "user_tickets",
            joinColumns = @JoinColumn(name = "tickets_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<UserEntity> userEntities;

    @ManyToMany
    @JoinTable(name = "owner_tickets",
            joinColumns = @JoinColumn(name = "tickets_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<UserEntity> ticketOwners;

    @OneToMany(cascade = CascadeType.REMOVE)
    List<TicketChangeLog> changeLog;

}
