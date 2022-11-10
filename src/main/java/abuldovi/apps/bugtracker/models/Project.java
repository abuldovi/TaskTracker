package abuldovi.apps.bugtracker.models;

import abuldovi.apps.bugtracker.states.StatusState;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
@Data
public class Project {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    int id;

    @NotNull
    @Size(min = 1, max = 20, message = "Name must be between 1 and 20 characters")
    String name;

    @NotNull(message = "Status cannot be null")
    StatusState status;

    @Size(min = 1, max = 1000, message = "Description must not be empty and must be less than 1000 characters")
    String description;

    LocalDateTime createdat;
    LocalDateTime changedat;

    @ManyToOne
    UserEntity createdby;

    @ManyToMany
            @JoinTable(name = "user_projects",
            joinColumns = @JoinColumn(name = "projects_id"),
                    inverseJoinColumns = @JoinColumn(name = "user_id")
            )
    List<UserEntity> userEntities;

    @ManyToMany
    @JoinTable(name = "owner_projects",
            joinColumns = @JoinColumn(name = "projects_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    List<UserEntity> projectOwners;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    List<Ticket> tickets;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    List<ProjectChangeLog> changes;

}
