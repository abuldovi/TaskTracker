package abuldovi.apps.bugtracker.services;

import abuldovi.apps.bugtracker.models.Project;
import abuldovi.apps.bugtracker.models.Ticket;
import abuldovi.apps.bugtracker.models.UserEntity;
import abuldovi.apps.bugtracker.repositories.TicketsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketsRepository ticketsRepository;
    private final UserService userService;
    private final TicketChangeLogService ticketChangeLogService;

    public TicketService(TicketsRepository ticketsRepository, UserService userService, ProjectChangeLogService projectChangeLogService, TicketChangeLogService ticketChangeLogService) {
        this.ticketsRepository = ticketsRepository;
        this.userService = userService;
        this.ticketChangeLogService = ticketChangeLogService;
    }

    public List<Ticket> findAll(){
        return ticketsRepository.findAll();
    }

    public List<Ticket> findAllByProjectId(int id){
        Project project = new Project();
        project.setId(id);
        return ticketsRepository.findAllByProjectId(id);
    }


    public Optional<Ticket> findOne(int id){
        return ticketsRepository.findById(id);
    }

    public Optional<Ticket> findOneWithProjectId(int id, int projectId){
        return ticketsRepository.findTicketByIdAndProjectId(id, projectId);
    }

    public Optional<Ticket> findTicketByNameAndProjectId(String name, int projectId){
        return ticketsRepository.findTicketByNameAndProjectId(name, projectId);
    }

    public Optional<Ticket> findOneByOwner(int id, String username){
        UserEntity userEntity = userService.findByUsername(username).get();
        if(ticketsRepository.findById(id).isPresent() && ticketsRepository.findById(id).get().getTicketOwners().contains(userEntity)){
            return ticketsRepository.findById(id);
        }
        return Optional.empty();
    }


    public boolean containsOwner(int id, String username){
        return findOneByOwner(id, username).isPresent();
    }

    public List<UserEntity> findOwners(int id){
        return ticketsRepository.findById(id).orElse(new Ticket()).getTicketOwners();
    }

    public void save(Ticket ticket, String createdby) {
        ticket.setCreatedat(LocalDateTime.now());
        UserEntity userEntity = userService.findByUsername(createdby).get();
        ticket.setCreatedby(userEntity);
        ticket.setTicketOwners(Collections.singletonList(userEntity));
        ticket.setUserEntities(Collections.singletonList(userEntity));
        ticketsRepository.save(ticket);
    }
    public void edit(Ticket ticketEdited, String madeBy) {
        Ticket ticket = findOne(ticketEdited.getId()).get();
        
        StringBuilder changes = new StringBuilder();
        if(!ticket.getName().equals(ticketEdited.getName())){
            changes.append("Name: ").append(ticket.getName()).append(" changed to ").append(ticketEdited.getName());
            ticket.setName(ticketEdited.getName());
        }
        if(!ticket.getStatus().equals(ticketEdited.getStatus())){
            changes.append("\n \\\\\\ Status: ").append(ticket.getStatus().label).append(" changed to ").append(ticketEdited.getStatus().label);
            ticket.setStatus(ticketEdited.getStatus());
        }
        if(!ticket.getDescription().equals(ticketEdited.getDescription())){
            changes.append("\n \\\\\\ Description: ").append(ticket.getDescription()).append(" changed to ").append(ticketEdited.getDescription());
            ticket.setDescription(ticketEdited.getDescription());
        }
        if(!(changes.length()==0)) {ticketChangeLogService.save(ticket.getId(), changes.toString(), madeBy); }
        ticket.setChangedat(LocalDateTime.now());
        ticketsRepository.save(ticket);
    }
    public void delete(Ticket ticketToDelete) {
        Ticket ticket = findOne(ticketToDelete.getId()).get();
        ticketsRepository.delete(ticket);
    }


    public void deleteOwner(UserEntity userEntity, int ticketId, String madeby){
        Optional<Ticket> ticket = ticketsRepository.findById(ticketId);
        ticket.get().getTicketOwners().remove(userService.findByUsername(userEntity.getUsername()).orElse(null));
        ticket.get().setChangedat(LocalDateTime.now());
        ticketChangeLogService.save(ticketId, "Owner " + userEntity.getUsername() + " was deleted", madeby);
        ticketsRepository.save(ticket.get());
    }

    public void addOwner(UserEntity userEntity, int ticketId, String madeby){
        Optional<Ticket> ticket = ticketsRepository.findById(ticketId);
        ticket.get().getTicketOwners().add(userService.findByUsername(userEntity.getUsername()).orElse(null));
        ticket.get().setChangedat(LocalDateTime.now());
        ticketChangeLogService.save(ticketId, "Owner " + userEntity.getUsername() + " was added", madeby);
        ticketsRepository.save(ticket.get());
    }

    public void deleteUser(UserEntity userEntity, int ticketId, String madeby){
        Optional<Ticket> ticket = ticketsRepository.findById(ticketId);
        UserEntity bdUser = userService.findByUsername(userEntity.getUsername()).orElse(null);
        ticket.get().getTicketOwners().remove(bdUser);
        ticket.get().getUserEntities().remove(bdUser);
        ticket.get().setChangedat(LocalDateTime.now());
        ticketChangeLogService.save(ticketId, "User " + userEntity.getUsername() + " was deleted", madeby);
        ticketsRepository.save(ticket.get());
    }

    public void addUser(UserEntity userEntity, int ticketId, String madeby){
        Optional<Ticket> ticket = ticketsRepository.findById(ticketId);
        ticket.get().getUserEntities().add(userService.findByUsername(userEntity.getUsername()).orElse(null));
        ticket.get().setChangedat(LocalDateTime.now());
        ticketChangeLogService.save(ticketId, "User " + userEntity.getUsername() + " was added", madeby);
        ticketsRepository.save(ticket.get());
    }

    public Optional<Ticket> findOneByUser(int id, String username){
        UserEntity userEntity = userService.findByUsername(username).get();
        if(ticketsRepository.findById(id).isPresent() && ticketsRepository.findById(id).get().getUserEntities().contains(userEntity)){
            return ticketsRepository.findById(id);
        }
        return Optional.empty();
    }

    public boolean containsUser(int id, String username){
        return findOneByUser(id, username).isPresent();
    }

    public List<Ticket> findAllByUsername(String username){
       return ticketsRepository.findAllByUserEntitiesContains(userService.findByUsername(username).get());
    }

    public Integer countTicketsByUsername(String username){
        return ticketsRepository.countAllByUserEntitiesContains(userService.findByUsername(username).get());
    }


}
