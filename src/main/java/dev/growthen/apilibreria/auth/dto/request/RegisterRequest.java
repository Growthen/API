package dev.growthen.apilibreria.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RegisterRequest {


    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "First name is required")
    @Size(min = 5, max = 100, message = "First name must be between 5 and 100 characters long")
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 5, max = 100, message = "Last name must be between 5 and 100 characters long")
    @Pattern(regexp = "^[a-zA-Z\\s]+$")
    private String lastName;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}
