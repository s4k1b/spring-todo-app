package com.example.todo.items.controller;

import com.example.todo.items.dto.TodoItemCreateDto;
import com.example.todo.items.dto.TodoItemInfoDto;
import com.example.todo.items.entity.TodoItem;
import com.example.todo.items.enums.TodoItemStatus;
import com.example.todo.items.mapper.TodoItemMapper;
import com.example.todo.items.service.TodoItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todo")
public class TodoController {
    private final TodoItemService todoItemService;
    public TodoController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTodo(@RequestBody TodoItemCreateDto todoItemCreateDto) {
        try {
            Long id = todoItemService.createTodoItem(todoItemCreateDto);
            return new ResponseEntity<>("Todo item added with id " + String.valueOf(id), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<TodoItemInfoDto>> getToDoList(@RequestParam Optional<String> status) {
        return ResponseEntity.ok(todoItemService.getTodoItems(status));
    }
}
