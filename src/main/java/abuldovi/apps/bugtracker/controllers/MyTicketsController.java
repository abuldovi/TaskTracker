package abuldovi.apps.bugtracker.controllers;

import abuldovi.apps.bugtracker.services.TicketService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mytickets")
public class MyTicketsController extends GlobalController{

    private final TicketService ticketService;



    public MyTicketsController(TicketService ticketService) {
        this.ticketService = ticketService;

    }

    @GetMapping("")
    public String main(Model model, Authentication authentication){
        model.addAttribute("tickets", ticketService.findAllByUsername(authentication.getName()));
        model.addAttribute("title", "My tickets");
        return "tickets/index";
    }
}
