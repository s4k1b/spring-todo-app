package com.example.todo.auth;

import lombok.Data;

@Data
public class AuthResponseDto {
    private String token;
    AuthResponseDto(String token) {
        this.token = token;
    }
}
