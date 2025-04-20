package com.example.todo.users.controller;

import java.net.URI;
import java.util.Optional;

import com.example.todo.users.dto.UserInfoDto;
import com.example.todo.users.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo.users.dto.UserRegistrationDto;
import com.example.todo.users.entity.User;
import com.example.todo.users.exceptions.UserAlreadyExistsException;
import com.example.todo.users.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<User> postMethodName(@Valid @RequestBody UserRegistrationDto userRegDto) throws UserAlreadyExistsException {
        User createdUser = userService.createUser(userRegDto);
        URI location = URI.create("/user/" + createdUser.getId());
        return ResponseEntity.created(location).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoDto> getUserInfo(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        if(user.isPresent()) {
            UserInfoDto userInfo = userMapper.userEntityToDto(user.get());
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
