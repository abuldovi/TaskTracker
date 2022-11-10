package abuldovi.apps.bugtracker.services;

import abuldovi.apps.bugtracker.models.Project;
import abuldovi.apps.bugtracker.models.UserEntity;
import abuldovi.apps.bugtracker.repositories.ProjectsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectsRepository projectsRepository;
    private final UserService userService;
    private final ProjectChangeLogService projectChangeLogService;
    private final TicketService ticketService;

    public ProjectService(ProjectsRepository projectsRepository, UserService userService, ProjectChangeLogService projectChangeLogService, TicketService ticketService) {
        this.projectsRepository = projectsRepository;
        this.userService = userService;
        this.projectChangeLogService = projectChangeLogService;
        this.ticketService = ticketService;
    }


    public List<UserEntity> findOwners(int id){
        return projectsRepository.findById(id).get().getProjectOwners();
    }
    public Optional<Project> findOne(int id){
        return projectsRepository.findById(id);
    }

    public Optional<Project> findOneByUser(int id, String username){
        UserEntity userEntity = userService.findByUsername(username).get();
        if(projectsRepository.findById(id).isPresent() && projectsRepository.findById(id).get().getUserEntities().contains(userEntity)){
            return projectsRepository.findById(id);
        }
        return Optional.empty();
    }

    public Optional<Project> findOneByOwner(int id, String username){
        UserEntity userEntity = userService.findByUsername(username).get();
        if(projectsRepository.findById(id).isPresent() && projectsRepository.findById(id).get().getProjectOwners().contains(userEntity)){
            return projectsRepository.findById(id);
        }
        return Optional.empty();
    }

    public void save(Project project, String createdby) {
        project.setCreatedat(LocalDateTime.now());
        UserEntity userEntity = userService.findByUsername(createdby).get();
        project.setCreatedby(userEntity);
        project.setProjectOwners(Collections.singletonList(userEntity));
        project.setUserEntities(Collections.singletonList(userEntity));
        projectsRepository.save(project);
    }


    public void edit(Project projectEdited, String name) {
        Project project = findOne(projectEdited.getId()).get();
        StringBuilder changes = new StringBuilder();
        if(!project.getName().equals(projectEdited.getName())){
            changes.append("Name: ").append(project.getName()).append(" changed to ").append(projectEdited.getName());
            project.setName(projectEdited.getName());
        }
        if(!project.getStatus().equals(projectEdited.getStatus())){
            changes.append("\n \\\\\\ Status: ").append(project.getStatus()).append(" changed to ").append(projectEdited.getStatus());
            project.setStatus(projectEdited.getStatus());
        }
        if(!project.getDescription().equals(projectEdited.getDescription())){
            changes.append("\n \\\\\\ Description: ").append(project.getDescription()).append(" changed to ").append(projectEdited.getDescription());
            project.setDescription(projectEdited.getDescription());
        }
        if(!(changes.length()==0)) {projectChangeLogService.save(project.getId(), changes.toString(), name); }
        project.setChangedat(LocalDateTime.now());
        projectsRepository.save(project);
    }
    public void delete(Project projectToDelete) {
        Project project = findOne(projectToDelete.getId()).get();
        projectsRepository.delete(project);
    }

    public boolean containsUser(int id, String username){
        return findOneByUser(id, username).isPresent();
    }

    public boolean containsOwner(int id, String username){
        return findOneByOwner(id, username).isPresent();
    }

    public void deleteOwner(UserEntity userEntity, int projectId, String madeby){
        Optional<Project> project = projectsRepository.findById(projectId);
        project.get().getProjectOwners().remove(userService.findByUsername(userEntity.getUsername()).orElse(null));
        project.get().setChangedat(LocalDateTime.now());
        projectChangeLogService.save(projectId, "Owner " + userEntity.getUsername() + " was deleted", madeby);
        projectsRepository.save(project.get());
    }

    public void addOwner(UserEntity userEntity, int projectId, String madeby){
        Optional<Project> project = projectsRepository.findById(projectId);
        project.get().getProjectOwners().add(userService.findByUsername(userEntity.getUsername()).orElse(null));
        project.get().setChangedat(LocalDateTime.now());
        projectChangeLogService.save(projectId, "Owner " + userEntity.getUsername() + " was added", madeby);
        projectsRepository.save(project.get());
    }

    public void deleteUser(UserEntity userEntity, int projectId, String madeby){
        Optional<Project> project = projectsRepository.findById(projectId);
        UserEntity bdUser = userService.findByUsername(userEntity.getUsername()).orElse(null);
        project.get().getProjectOwners().remove(bdUser);
        project.get().getUserEntities().remove(bdUser);
        project.get().setChangedat(LocalDateTime.now());
        project.get().getTickets().forEach(x->ticketService.deleteOwner(bdUser, x.getId(), madeby));
        project.get().getTickets().forEach(x->ticketService.deleteUser(bdUser, x.getId(), madeby));
        projectChangeLogService.save(projectId, "User " + userEntity.getUsername() + " was deleted", madeby);
        projectsRepository.save(project.get());
    }

    public void addUser(UserEntity userEntity, int projectId, String madeby){
        Optional<Project> project = projectsRepository.findById(projectId);
        project.get().getUserEntities().add(userService.findByUsername(userEntity.getUsername()).orElse(null));
        project.get().setChangedat(LocalDateTime.now());
        projectChangeLogService.save(projectId, "User " + userEntity.getUsername() + " was added", madeby);
        projectsRepository.save(project.get());
    }

    public Integer countProjectsByUsername(String username){
        return projectsRepository.countAllByUserEntitiesContains(userService.findByUsername(username).get());
    }




}
