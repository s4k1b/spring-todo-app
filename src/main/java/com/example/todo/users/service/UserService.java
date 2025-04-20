package com.example.todo.users.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.todo.users.dto.UserRegistrationDto;
import com.example.todo.users.entity.User;
import com.example.todo.users.exceptions.UserAlreadyExistsException;
import com.example.todo.users.mapper.UserMapper;
import com.example.todo.users.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User createUser(UserRegistrationDto userRegDto) {

        if (userRepository.existsByEmail(userRegDto.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + userRegDto.getEmail() + " already exists");
        }

        userRegDto.setPassword(passwordEncoder.encode(userRegDto.getPassword()));
        User user = userMapper.userRegistrationDtoToEntity(userRegDto);
        return userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> findUserById(Long id) {return userRepository.findById(id);}
}
