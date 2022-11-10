package abuldovi.apps.bugtracker.validators;

import abuldovi.apps.bugtracker.models.UserEntity;
import abuldovi.apps.bugtracker.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserEntityValidator implements Validator {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserEntityValidator(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return UserEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserEntity userEntity = (UserEntity) target;
        Optional<UserEntity> userDB = userService.findByUsername(userEntity.getUsername());
        if(userEntity.getPasswordRepeat()!=null && !passwordEncoder.matches(userEntity.getPasswordRepeat(),
                                    userDB.get().getPassword())){
            errors.rejectValue("passwordRepeat", "", "Password is incorrect");
        }
        if(userEntity.getPasswordRepeat()==null && userDB.isPresent()){
            errors.rejectValue("username", "", "User with such name exists");
        }
    }
}
