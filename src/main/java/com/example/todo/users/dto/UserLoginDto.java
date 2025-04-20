package com.example.todo.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDto {
    @NotBlank(message = "User email is required")
    private String email;
    @NotBlank(message = "User password is required")
    private String password;
}
