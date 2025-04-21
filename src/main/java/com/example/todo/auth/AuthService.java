package com.example.todo.auth;

import com.example.todo.security.JwtUtils;
import com.example.todo.users.dto.UserInfoDto;
import com.example.todo.users.entity.User;
import com.example.todo.users.mapper.UserMapper;
import com.example.todo.users.repository.UserRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuditorAware<User> auditorAware;
    public AuthService(UserRepository userRepository, UserMapper userMapper, JwtUtils jwtUtils, AuditorAware<User> auditorAware) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.auditorAware = auditorAware;
    }
    public String authenticateUser(String userEmail, String password) {
        User user = userRepository.findByEmail(userEmail);
        UserInfoDto userInfo = userMapper.userEntityToDto(user);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtils.generateJwt(userInfo);
        }
        throw new RuntimeException("Invalid credentials");
    }
    public Optional<User> getAuthenticatedUser() {
        return auditorAware.getCurrentAuditor();
    }
}
