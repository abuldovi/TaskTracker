package abuldovi.apps.bugtracker.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "project_changelog")
@Data
public class ProjectChangeLog {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    int id;
    @ManyToOne
    @JoinColumn(name="project_id", nullable=false)
    Project project;

    String description;


    LocalDateTime createdat;

    @ManyToOne
    @JoinColumn(name="madeby_id", nullable=false)
    UserEntity madeby;

}

