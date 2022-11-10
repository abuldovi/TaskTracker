package abuldovi.apps.bugtracker.controllers;

import abuldovi.apps.bugtracker.models.FormPassword;
import abuldovi.apps.bugtracker.models.UserEntity;
import abuldovi.apps.bugtracker.security.UserDetails;
import abuldovi.apps.bugtracker.services.UserService;
import abuldovi.apps.bugtracker.validators.FormPasswordValidator;
import abuldovi.apps.bugtracker.validators.UserEntityValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController extends GlobalController{

    private final UserService userService;
    private final UserEntityValidator userEntityValidator;
    private final FormPasswordValidator formPasswordValidator;

    public ProfileController(UserService userService, UserEntityValidator userEntityValidator, FormPasswordValidator formPasswordValidator) {
        this.userService = userService;
        this.userEntityValidator = userEntityValidator;
        this.formPasswordValidator = formPasswordValidator;
    }


    @GetMapping("")
    public String showProfile(Model model, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("title", authentication.getName());
        model.addAttribute("userEntity", userService.findByUsername(userDetails.getUsername()).get());
        return "profile/myprofile";
    }
    @GetMapping("edit")
    public String editProfile(Model model, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("title", "Edit profile");
        model.addAttribute("userEntity", userService.findByUsername(userDetails.getUsername()).get());
        return "profile/edit";
    }

    @PatchMapping("edit")
    public String editProfilePush(Model model, @ModelAttribute("userEntity") @Valid UserEntity userEntity, BindingResult bindingResult, Authentication authentication){
        userEntityValidator.validate(userEntity, bindingResult);
        if(bindingResult.hasErrors()){
            model.addAttribute("title", "Edit profile");
            return "profile/edit";
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity principalEntity = userDetails.getUserEntity();
        userService.editUser(userEntity, principalEntity);
        return "redirect:/profile";
    }

    @GetMapping("edit/changePassword")
    public String changePassword(Model model, Authentication authentication){
        model.addAttribute("title", "Change password");
        model.addAttribute("formPassword", new FormPassword());
        return "profile/changePassword";
    }

    @GetMapping("{username}")
    public String changePassword(@PathVariable("username") String username, Model model, Authentication authentication){
        model.addAttribute("title");
        model.addAttribute("userEntity", userService.findByUsername(username));
        return "profile/profile";
    }

    @PatchMapping("edit/changePassword")
    public String changePasswordPush(Model model, @ModelAttribute("formPassword") @Valid FormPassword formPassword, BindingResult bindingResult, Authentication authentication){
        formPassword.setUsername(authentication.getName());
        formPasswordValidator.validate(formPassword, bindingResult);
        if(bindingResult.hasErrors()){
            model.addAttribute("title", "Change password");
            return "profile/changePassword";
        }
        userService.changePassword(authentication.getName(), formPassword);
        return "redirect:/";
    }


}
