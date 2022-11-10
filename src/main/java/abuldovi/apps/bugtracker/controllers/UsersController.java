package abuldovi.apps.bugtracker.controllers;

import abuldovi.apps.bugtracker.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UsersController extends GlobalController{

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{username}")
    public String changePassword(@PathVariable("username") String username, Model model, Authentication authentication){
        model.addAttribute("title", username);
        model.addAttribute("userEntity", userService.findByUsername(username));
        return "profile/profile";
    }

}
