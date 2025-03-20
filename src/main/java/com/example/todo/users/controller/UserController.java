package com.example.todo.users.controller;

import java.net.URI;

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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> postMethodName(@Valid @RequestBody UserRegistrationDto userRegDto) throws UserAlreadyExistsException {
        User createdUser = userService.createUser(userRegDto);
        URI location = URI.create("/user/" + createdUser.getId());
        return ResponseEntity.created(location).body(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserInfo(@PathVariable Long id) {
        return ResponseEntity.ok("User: " + id.toString());
    }

}
