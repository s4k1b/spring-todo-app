package com.example.todo.items.service;

import com.example.todo.auth.AuthService;
import com.example.todo.items.dto.TodoItemCreateDto;
import com.example.todo.items.dto.TodoItemInfoDto;
import com.example.todo.items.entity.TodoItem;
import com.example.todo.items.enums.TodoItemStatus;
import com.example.todo.items.mapper.TodoItemMapper;
import com.example.todo.items.repository.TodoItemRepository;
import com.example.todo.users.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoItemService {
    private final TodoItemRepository todoItemRepository;
    private final TodoItemMapper todoItemMapper;
    private final AuthService authService;

    public TodoItemService(TodoItemRepository todoItemRepository, TodoItemMapper todoItemMapper, AuthService authService) {
        this.todoItemRepository = todoItemRepository;
        this.todoItemMapper = todoItemMapper;
        this.authService = authService;
    }

    @Transactional
    public Long createTodoItem(TodoItemCreateDto todoItemCreateDto) {
        TodoItem todoItem = todoItemMapper.todoItemCreateDtoToEntity(todoItemCreateDto);
        todoItem.setStatus(TodoItemStatus.PENDING);
        return todoItemRepository.save(todoItem).getId();
    }

    public List<TodoItemInfoDto> getTodoItems() {
        User user = authService.getAuthenticatedUser().orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getTodoItems().stream().map(todoItemMapper::todoItemEntityToInfo).toList();
    }
}
