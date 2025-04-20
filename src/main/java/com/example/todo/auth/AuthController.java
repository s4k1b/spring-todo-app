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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    public AuthController(UserRepository userRepository, JwtUtils jwtUtils, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody UserLoginDto userLoginDto) {
        User user = userRepository.findByEmail(userLoginDto.getEmail());
        UserInfoDto userInfo = userMapper.userEntityToDto(user);
        if (user != null && passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            String token = jwtUtils.generateJwt(userInfo);
            return ResponseEntity.ok(new AuthResponseDto(token));
        }
        throw new RuntimeException("Invalid credentials");
    }
}
