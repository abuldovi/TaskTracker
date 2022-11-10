package abuldovi.apps.bugtracker.controllers;

import abuldovi.apps.bugtracker.models.UserEntity;
import abuldovi.apps.bugtracker.services.RegistrationService;
import abuldovi.apps.bugtracker.validators.UserEntityValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;


@Controller
public class LoginController {

    private final RegistrationService registrationService;
    private final UserEntityValidator userEntityValidator;

    public LoginController(RegistrationService registrationService, UserEntityValidator userEntityValidator) {
        this.registrationService = registrationService;
        this.userEntityValidator = userEntityValidator;
    }

    @GetMapping("login")
    public String loginPage(Model model){
        model.addAttribute("title", "Login");
        return "auth/login";
    }

    @PostMapping("register")
    public String register(@ModelAttribute("user") @Valid UserEntity userEntity, BindingResult bindingResult, Model model){
        userEntityValidator.validate(userEntity, bindingResult);
        if(bindingResult.hasErrors()){
            model.addAttribute("title", "Registration");
            return "auth/register";
        }
        registrationService.register(userEntity);
        return "redirect:/auth/login";
    }

    @GetMapping("register")
    public String registerPage(@ModelAttribute("user") UserEntity userEntity, Model model){
        model.addAttribute("title", "Registration");
        return "auth/register";
    }
}
