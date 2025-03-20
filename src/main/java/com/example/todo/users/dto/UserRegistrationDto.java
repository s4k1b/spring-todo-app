package com.example.todo.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotBlank(message = "User name is required")
    private String name;

    @NotBlank(message = "User email is required")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Password field is required")
    private String password;

    @NotBlank(message = "Re Type Password field is required")
    private String reTypePassword;
}
