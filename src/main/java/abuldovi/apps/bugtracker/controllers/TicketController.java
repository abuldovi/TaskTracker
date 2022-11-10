package abuldovi.apps.bugtracker.controllers;

import abuldovi.apps.bugtracker.models.Ticket;
import abuldovi.apps.bugtracker.models.TicketChangeLog;
import abuldovi.apps.bugtracker.models.UserEntity;
import abuldovi.apps.bugtracker.services.*;
import abuldovi.apps.bugtracker.states.StatusState;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/projects/{projectId}/ticket/")
public class TicketController extends GlobalController{
   private final TicketService ticketService;
    private final ProjectService projectService;
    private final ProjectChangeLogService projectChangeLogService;
    private final UserService userService;

    private final TicketChangeLogService ticketChangeLogService;

    public TicketController(TicketService ticketService, ProjectService projectService, ProjectChangeLogService projectChangeLogService, UserService userService, TicketChangeLogService ticketChangeLogService) {
        this.ticketService = ticketService;
        this.projectService = projectService;
        this.projectChangeLogService = projectChangeLogService;
        this.userService = userService;
        this.ticketChangeLogService = ticketChangeLogService;
    }

    @GetMapping("{id}")
    public String ticketName(@PathVariable("id") int id, @PathVariable("projectId") int projectId, Model model, Authentication authentication){
        if(!projectService.containsUser(projectId, authentication.getName())) return "error/404";
        Optional<Ticket> ticketByUser =  ticketService.findOneByUser(id, authentication.getName());
        Optional<Ticket> ticketByProjectId =  ticketService.findOneWithProjectId(id, projectId);
        model.addAttribute("ticket", ticketByProjectId);
        model.addAttribute("title", ticketByProjectId.get().getName());
        model.addAttribute("owner", ticketService.containsOwner(id, authentication.getName()));
        model.addAttribute("executor", ticketByUser.isPresent());
        model.addAttribute("owners", ticketService.findOwners(id));
        model.addAttribute("users", userService.findAllExceptTicket(id, projectId));
        model.addAttribute("userEntity", new UserEntity());
        model.addAttribute("changeLog", new TicketChangeLog());
        model.addAttribute("changes", ticketChangeLogService.findAllByTicket(ticketByProjectId.get()));
        return "tickets/ticket";
    }

    @GetMapping("create")
    public String create(@ModelAttribute("ticket") Ticket ticket, Model model, @PathVariable("projectId") int projectId, Authentication authentication){
        if(!projectService.containsUser(projectId, authentication.getName())) return "error/404";
        model.addAttribute("title", "Create ticket");
        model.addAttribute("statuses", StatusState.values());
        model.addAttribute("projectId", projectId);
        model.addAttribute("projectEntity", projectService.findOneByUser(projectId, authentication.getName()));
        return "tickets/create";
    }

    @PostMapping("create")
    public String createPush(@ModelAttribute("ticket") @Valid Ticket ticket, BindingResult bindingResult, Model model, @PathVariable("projectId") int projectId, Authentication authentication){
        if(!projectService.containsUser(projectId, authentication.getName())) return "error/404";
        if(bindingResult.hasErrors()){
            return create(ticket, model.addAttribute(bindingResult.getModel()), projectId, authentication);
        }
        ticketService.save(ticket, authentication.getName());
        projectChangeLogService.save(projectId,  "New ticket " + ticket.getName() + " was added", authentication.getName());
        return "redirect:/projects/"+projectId+"/";
    }

    @GetMapping("{id}/edit")
    public String edit(Model model, @PathVariable("id") int id, @PathVariable("projectId") int projectId, Authentication authentication){
        if(!projectService.containsUser(projectId, authentication.getName())) return "error/404";
        model.addAttribute("ticket", ticketService.findOneWithProjectId(id, projectId).get());
        model.addAttribute("statuses", StatusState.values());
        model.addAttribute("title", "Edit ticket");
        return "tickets/edit";
    }

    @PatchMapping("{id}/edit")
    public String editPush(Model model, @ModelAttribute("ticket") @Valid Ticket ticket, BindingResult bindingResult, @PathVariable("id") int id, @PathVariable("projectId") int projectId, Authentication authentication){
        if(!projectService.containsUser(projectId, authentication.getName())) return "error/404";
        if(bindingResult.hasErrors()){
            // Костыли, так как не смог нормально передать в модели все, что нужно с аттрибутами)
            ticket.setId(id);
            ticket.setName(ticketService.findOne(id).get().getName());
            model.addAttribute("ticket", ticket);
            model.addAttribute("statuses", StatusState.values());
            model.addAttribute("title", "Edit ticket");
            return "tickets/edit";
        }
        ticketService.edit(ticket, authentication.getName());
        return "redirect:/projects/" + projectId + "/ticket/" + id + "/";

    }

    @GetMapping("{id}/delete")
    public String delete(@PathVariable("id") int id, Model model, @PathVariable("projectId") int projectId, Authentication authentication){
        if(!projectService.containsUser(projectId, authentication.getName())) return "error/404";
        model.addAttribute("ticket", ticketService.findOne(id));
        model.addAttribute("title", "Delete ticket");
        return "tickets/delete";
    }

    @DeleteMapping ("{id}/delete")
    public String deletePush(@ModelAttribute("ticket") Ticket ticket, @PathVariable("projectId") int projectId, Authentication authentication){
        if(!projectService.containsUser(projectId, authentication.getName())) return "error/404";
        ticketService.delete(ticket);
        projectChangeLogService.save(projectId,  "Ticket " + ticket.getName() + " was deleted", authentication.getName());
        return "redirect:/projects"+projectId;
    }

    @PostMapping("{id}/deleteOwner")
    public String deleteOwner(@ModelAttribute("participant") UserEntity userEntity, @PathVariable("id") int id, @PathVariable("projectId") int projectId, Authentication authentication){
        if(!ticketService.containsOwner(id, authentication.getName())) return "error/404";
        ticketService.deleteOwner(userEntity, id, authentication.getName());
        return "redirect:";
    }

    @PostMapping("{id}/makeOwner")
    public String makeOwner(@ModelAttribute("participant") UserEntity userEntity, @PathVariable("id") int id, @PathVariable("projectId") int projectId, Authentication authentication){
        if(!ticketService.containsOwner(id, authentication.getName())) return "error/404";
        ticketService.addOwner(userEntity, id, authentication.getName());
        return "redirect:";
    }

    @PostMapping("{id}/deleteUser")
    public String deleteUser(@ModelAttribute("participant") UserEntity userEntity, @PathVariable("id") int id, @PathVariable("projectId") int projectId, Authentication authentication){
        if(!ticketService.containsOwner(id, authentication.getName())) return "error/404";
        ticketService.deleteUser(userEntity, id, authentication.getName());
        return "redirect:";
    }

    @PostMapping("{id}/addUser")
    public String addUser(@ModelAttribute("userEntity") UserEntity userEntity, @PathVariable("id") int id, @PathVariable("projectId") int projectId, Authentication authentication){
        if(!ticketService.containsOwner(id, authentication.getName())) return "error/404";
        ticketService.addUser(userEntity, id, authentication.getName());
        return "redirect:";
    }

    @PostMapping("{id}/addChange")
    public String addChange(@ModelAttribute("changeLog") TicketChangeLog ticketChangeLog, @PathVariable("id") int id, @PathVariable("projectId") int projectId, Authentication authentication){
        if(!ticketService.containsUser(id, authentication.getName())) return "error/404";
        ticketChangeLogService.save(id, ticketChangeLog.getDescription(), authentication.getName());
        return "redirect:";
    }

}
