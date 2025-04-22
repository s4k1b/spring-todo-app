package com.example.todo.items.service;

import com.example.todo.auth.AuthService;
import com.example.todo.items.dto.TodoItemCreateDto;
import com.example.todo.items.dto.TodoItemInfoDto;
import com.example.todo.items.dto.TodoItemUpdateDto;
import com.example.todo.items.entity.TodoItem;
import com.example.todo.items.enums.TodoItemStatus;
import com.example.todo.items.mapper.TodoItemMapper;
import com.example.todo.items.repository.TodoItemRepository;
import com.example.todo.users.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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

    private User getAutheticatedUser() {
        return authService.getAuthenticatedUser().orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    private void assertTodoOwnership(TodoItem todoItem) throws ResponseStatusException {
        boolean isOwner = todoItem.getCreatedBy().equals(getAutheticatedUser());
        if(!isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @Transactional
    public Long createTodoItem(TodoItemCreateDto todoItemCreateDto) {
        TodoItem todoItem = todoItemMapper.todoItemCreateDtoToEntity(todoItemCreateDto);
        todoItem.setStatus(TodoItemStatus.PENDING);
        return todoItemRepository.save(todoItem).getId();
    }

    public List<TodoItemInfoDto> getTodoItems(Optional<String> status) {
        User user = getAutheticatedUser();
        return user.getTodoItems().stream().filter((todo) -> status.isEmpty() || todo.getStatus().toString().equalsIgnoreCase(status.get())).map(todoItemMapper::todoItemEntityToInfo).toList();
    }

    public TodoItem getTodoItemById(Long id) {
        TodoItem todoItem = todoItemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        assertTodoOwnership(todoItem);
        return todoItem;
    }

    @Transactional
    public Long updateStatus(TodoItem todoItem, TodoItemStatus status) {
        assertTodoOwnership(todoItem);
        todoItem.setStatus(status);
        return todoItemRepository.save(todoItem).getId();
    }

    @Transactional
    public Long update(TodoItem todoItem, TodoItemUpdateDto todoItemUpdateDto) {
        assertTodoOwnership(todoItem);

        if(todoItemUpdateDto.getTitle() != null) {
            todoItem.setTitle(todoItemUpdateDto.getTitle());
        }
        return todoItemRepository.save(todoItem).getId();
    }

    @Transactional
    public void deleteTodoItem(TodoItem todoItem) {
        assertTodoOwnership(todoItem);
        todoItemRepository.delete(todoItem);
    }
}
