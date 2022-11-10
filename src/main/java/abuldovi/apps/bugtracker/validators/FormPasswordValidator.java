package abuldovi.apps.bugtracker.validators;

import abuldovi.apps.bugtracker.models.FormPassword;
import abuldovi.apps.bugtracker.models.UserEntity;
import abuldovi.apps.bugtracker.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormPasswordValidator implements Validator {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public FormPasswordValidator(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return FormPassword.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FormPassword formPassword = (FormPassword) target;
        if(!passwordEncoder.matches(formPassword.getOldPassword(),
                                    userService.findByUsername(formPassword.getUsername()).get().getPassword())){
            errors.rejectValue("oldPassword", "", "Password is incorrect");
        }
        if(!formPassword.getNewPassword().equals(formPassword.getPasswordRepeat())){
            errors.rejectValue("passwordRepeat", "", "Passwords do not match");
        }

    }
}
