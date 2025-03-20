package com.example.todo.users.service;

import org.springframework.stereotype.Service;

import com.example.todo.users.dto.UserRegistrationDto;
import com.example.todo.users.entity.User;
import com.example.todo.users.exceptions.UserAlreadyExistsException;
import com.example.todo.users.mapper.UserMapper;
import com.example.todo.users.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User createUser(UserRegistrationDto userRegDto) {

        if (userRepository.existsByEmail(userRegDto.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + userRegDto.getEmail() + " already exists");
        }

        User user = userMapper.userRegistrationDtoToEntity(userRegDto);
        User createdUser = userRepository.save(user);
        return createdUser;

    }
}
