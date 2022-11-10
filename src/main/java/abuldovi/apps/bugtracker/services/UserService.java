package abuldovi.apps.bugtracker.services;

import abuldovi.apps.bugtracker.models.FormPassword;
import abuldovi.apps.bugtracker.models.Project;
import abuldovi.apps.bugtracker.models.Ticket;
import abuldovi.apps.bugtracker.models.UserEntity;
import abuldovi.apps.bugtracker.repositories.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService{
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }



    public Optional<UserEntity> findByUsername(String username){
        return usersRepository.findUserByUsername(username);
    }

    public List<UserEntity> findAllExceptProject(int id){
        return usersRepository.findAll()
                .stream()
                .filter(x->x.getProjectList()
                        .stream()
                        .map(y->y.getId())
                        .allMatch(s->s!=id))
                .collect(Collectors.toList());
    }

    public List<UserEntity> findAllExceptTicket(int id, int projectId){
        var list = usersRepository.findAll()
                .stream()
                .filter(x->x.getProjectList()
                        .stream()
                        .map(Project::getId)
                        .anyMatch(s->s==projectId)
                )
                .filter(x->x.getTicketList()
                        .stream()
                        .map(Ticket::getId)
                        .allMatch(s->s!=id))
                .collect(Collectors.toList());
        return list;


    }

    public Integer countDaysInCompany(String username){
        LocalDateTime createdat = findByUsername(username).get().getCreatedat();
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        return (int)(now.atZone(zoneId).toEpochSecond() - createdat.atZone(zoneId).toEpochSecond())/86400;
    }



    public void editUser(UserEntity editedUser, UserEntity principal){
        if(passwordEncoder.matches(editedUser.getPasswordRepeat(), principal.getPassword())){
            UserEntity userEntity = findByUsername(principal.getUsername()).get();
            userEntity.setFirstname(editedUser.getFirstname());
            userEntity.setSurname(editedUser.getSurname());
            userEntity.setPosition(editedUser.getPosition());
            userEntity.setEmail(editedUser.getEmail());
            userEntity.setEmail(editedUser.getEmail());
            if(!editedUser.getPassword().equals("passwordNull1")){
                userEntity.setPassword(editedUser.getPassword());
            }
            userEntity.setChangedat(LocalDateTime.now());
            usersRepository.save(userEntity);
        }

    }

    public void changePassword(String username, FormPassword formPassword){
        UserEntity userEntity = findByUsername(username).get();
        if(passwordEncoder.matches(formPassword.getOldPassword(), userEntity.getPassword())){
            userEntity.setPassword(passwordEncoder.encode(formPassword.getNewPassword()));
            usersRepository.save(userEntity);
        }

    }



}
