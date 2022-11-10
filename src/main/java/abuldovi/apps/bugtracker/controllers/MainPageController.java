package abuldovi.apps.bugtracker.controllers;

import abuldovi.apps.bugtracker.services.ProjectService;
import abuldovi.apps.bugtracker.services.TicketService;
import abuldovi.apps.bugtracker.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController extends GlobalController{

    private final TicketService ticketService;
    private final ProjectService projectService;
    private final UserService userService;

    public MainPageController(TicketService ticketService, ProjectService projectService, UserService userService) {
        this.ticketService = ticketService;
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    public String getMain(Model model, Authentication authentication){
        model.addAttribute("title", "Dashboard");
        model.addAttribute("ticketCount", ticketService.countTicketsByUsername(authentication.getName()));
        model.addAttribute("projectCount", projectService.countProjectsByUsername(authentication.getName()));
        model.addAttribute("daysCount", userService.countDaysInCompany(authentication.getName()));
        return "index";
    }

}
