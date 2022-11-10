package abuldovi.apps.bugtracker.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

public class GlobalController {
    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication) {
        model.addAttribute("principalName", authentication.getName());
    }
}
