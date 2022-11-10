package abuldovi.apps.bugtracker.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class FormPassword {
    private String username;
    @NotEmpty(message = "This field must not be empty")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$", message = "Password must have eight characters and at least one letter and one number")
    private String newPassword;
    @NotEmpty(message = "This field must not be empty")
    private String passwordRepeat;
    @NotNull
    private String oldPassword;
}
