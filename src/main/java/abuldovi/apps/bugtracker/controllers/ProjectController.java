package abuldovi.apps.bugtracker.controllers;

import abuldovi.apps.bugtracker.models.Project;
import abuldovi.apps.bugtracker.models.UserEntity;
import abuldovi.apps.bugtracker.services.ProjectChangeLogService;
import abuldovi.apps.bugtracker.services.ProjectService;
import abuldovi.apps.bugtracker.services.TicketService;
import abuldovi.apps.bugtracker.services.UserService;
import abuldovi.apps.bugtracker.states.StatusState;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/projects")
public class ProjectController extends GlobalController{
   private final ProjectService projectService;
   private final TicketService ticketService;
   private final UserService userService;
   private final ProjectChangeLogService projectChangeLogService;

    public ProjectController(ProjectService projectService, TicketService ticketService, UserService userService, ProjectChangeLogService projectChangeLogService) {
        this.projectService = projectService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.projectChangeLogService = projectChangeLogService;
    }

    @GetMapping("")
    public String main(Model model, Authentication authentication){
        model.addAttribute("projects", userService.findByUsername(authentication.getName()).get().getProjectList());
        model.addAttribute("title", "My projects");
        return "projects/index";
    }

    @GetMapping("/{id}")
    public String project(@PathVariable("id") int id, Model model, Authentication authentication){
        if(!projectService.containsUser(id, authentication.getName())) return "error/404";
        Optional<Project> project =  projectService.findOneByUser(id, authentication.getName());
        model.addAttribute("project", project);
        model.addAttribute("title", project.get().getName());
        model.addAttribute("tickets", ticketService.findAllByProjectId(id));
        model.addAttribute("changes", projectChangeLogService.findAllByProject(project.get()));
        model.addAttribute("owner", projectService.containsOwner(id, authentication.getName()));
        model.addAttribute("owners", projectService.findOwners(id));
        model.addAttribute("users", userService.findAllExceptProject(id));
        model.addAttribute("userEntity", new UserEntity());
        return "projects/project";
    }

    @GetMapping("/create")
    public String create(@ModelAttribute("project") Project project, Model model){
        model.addAttribute("title", "Create project");
        model.addAttribute("statuses", StatusState.values());
        return "projects/create";
    }

    @PostMapping("/create")
    public String createPush(@ModelAttribute("project") @Valid Project project, BindingResult bindingResult, Model model, Authentication authentication){
        if(bindingResult.hasErrors()){
            return create(project, model.addAttribute(bindingResult.getModel()));
        }
        projectService.save(project, authentication.getName());
        return "redirect:/projects";
    }

    @GetMapping("{id}/edit")
    public String edit(Model model, @PathVariable("id") int id, Authentication authentication){
        if(!projectService.containsOwner(id, authentication.getName())) return "error/404";
        model.addAttribute("project", projectService.findOne(id).get());
        model.addAttribute("statuses", StatusState.values());
        model.addAttribute("title", "Edit project");
        return "projects/edit";
    }

    @PostMapping("{id}/deleteOwner")
    public String deleteOwner(@ModelAttribute("participant")UserEntity userEntity, @PathVariable("id") int id, Authentication authentication){
        if(!projectService.containsOwner(id, authentication.getName())) return "error/404";
        projectService.deleteOwner(userEntity, id, authentication.getName());
        return "redirect:";
    }

    @PostMapping("{id}/makeOwner")
    public String makeOwner(@ModelAttribute("participant") UserEntity userEntity, @PathVariable("id") int id, Authentication authentication){
        if(!projectService.containsOwner(id, authentication.getName())) return "error/404";
        projectService.addOwner(userEntity, id, authentication.getName());
        return "redirect:";
    }

    @PostMapping("{id}/deleteUser")
    public String deleteUser(@ModelAttribute("participant") UserEntity userEntity, @PathVariable("id") int id, Authentication authentication){
        if(!projectService.containsOwner(id, authentication.getName())) return "error/404";
        projectService.deleteUser(userEntity, id, authentication.getName());
        return "redirect:";
    }

    @PostMapping("{id}/addUser")
    public String addUser(@ModelAttribute("userEntity") UserEntity userEntity, @PathVariable("id") int id, Authentication authentication){
        if(!projectService.containsOwner(id, authentication.getName())) return "error/404";
        projectService.addUser(userEntity, id, authentication.getName());
        return "redirect:";
    }

    @PatchMapping("{id}/edit")
    public String editPush( Model model, @ModelAttribute("project") @Valid Project project, BindingResult bindingResult, @PathVariable("id") int id, Authentication authentication){
        if(!projectService.containsOwner(id, authentication.getName())) return "error/404";
        if(bindingResult.hasErrors()){
            // Костыли, так как не смог нормально передать в модели все, что нужно с аттрибутами)
            project.setId(id);
            project.setName(projectService.findOne(id).get().getName());
            model.addAttribute("project", project);
            model.addAttribute("statuses", StatusState.values());
            model.addAttribute("title", "Edit project");
            return "projects/edit";
        }
        projectService.edit(project, authentication.getName());
        return "redirect:/projects/" + id;

    }

    @GetMapping("{id}/delete")
    public String delete(@PathVariable("id") int id, Model model, Authentication authentication){
        if(!projectService.containsOwner(id, authentication.getName())) return "error/404";
        model.addAttribute("project", projectService.findOne(id));
        model.addAttribute("title", "Delete project");
        return "projects/delete";
    }

    @DeleteMapping ("{id}/delete")
    public String deletePush(@PathVariable("id") int id, @ModelAttribute("project") Project project, Authentication authentication){
        if(!projectService.containsOwner(id, authentication.getName())) return "error/404";
        projectService.delete(project);
        return "redirect:/projects";
    }

}
