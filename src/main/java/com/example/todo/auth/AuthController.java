package com.example.todo.auth;

import com.example.todo.security.JwtUtils;
import com.example.todo.users.dto.UserInfoDto;
import com.example.todo.users.dto.UserLoginDto;
import com.example.todo.users.entity.User;
import com.example.todo.users.mapper.UserMapper;
import com.example.todo.users.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody UserLoginDto userLoginDto) {
        String token = authService.authenticateUser(userLoginDto.getEmail(), userLoginDto.getPassword());
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
