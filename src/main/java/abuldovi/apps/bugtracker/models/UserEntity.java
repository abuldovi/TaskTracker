package abuldovi.apps.bugtracker.models;


import lombok.Data;
import org.checkerframework.checker.regex.qual.Regex;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "userentity")
@Data
public class UserEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    int id;

    @NotNull
    @Size(min = 3, max = 20, message = "Username must be between 1 and 60 characters")
    String username;

    @NotNull
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password must have eight characters and at least one letter and one number")
    String password;
    @Size(min = 1, max = 60, message = "First name must be between 1 and 60 characters")
    String firstname;
    @Size(min = 1, max = 60, message = "Surname name must be between 1 and 60 characters")
    String surname;
    @Size(max = 20, message = "Position must be between 1 and 20 characters")
    String position;

    LocalDateTime createdat;
    LocalDateTime changedat;
    String role = "ROLE_USER";

    @Email(message = "Email format is incorrect")
    String email;

    @ManyToMany(mappedBy = "userEntities")
    List<Project> projectList;

    @ManyToMany(mappedBy = "projectOwners")
    List<Project> projectOwned;

    @ManyToMany(mappedBy = "userEntities")
    List<Ticket> ticketList;

    @ManyToMany(mappedBy = "ticketOwners")
    List<Ticket> ticketsOwned;

    @OneToMany(mappedBy = "madeby")
    List<ProjectChangeLog> changes;

    @Transient
    String passwordRepeat;

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", position='" + position + '\'' +
                ", createdat=" + createdat +
                ", changedat=" + changedat +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", passwordRepeat='" + passwordRepeat + '\'' +
                '}';
    }
}
