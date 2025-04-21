package com.example.todo.auth;

import com.example.todo.users.dto.UserInfoDto;
import com.example.todo.users.entity.User;
import com.example.todo.users.repository.UserRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<User> {
    private final UserRepository userRepository;
    AuditorAwareImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public Optional<User> getCurrentAuditor() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        UserInfoDto userInfoDto = (UserInfoDto) authentication.getPrincipal();
        return userRepository.findById(userInfoDto.getId());
    }
}
