package com.example.todo.items.controller;

import com.example.todo.items.dto.TodoItemCreateDto;
import com.example.todo.items.dto.TodoItemUpdateDto;
import com.example.todo.items.entity.TodoItem;
import com.example.todo.items.enums.TodoItemStatus;
import com.example.todo.items.service.TodoItemService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo/{id}")
public class TodoItemController {
    private TodoItem todoItem;

    private final TodoItemService todoItemService;
    public TodoItemController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @ModelAttribute
    public void getTodoItem(@PathVariable("id") Long id) {
        this.todoItem = todoItemService.getTodoItemById(id);
    }

    @PatchMapping("/done")
    public ResponseEntity<String> todoItemDone() {
        Long updatedId = todoItemService.updateStatus(this.todoItem, TodoItemStatus.DONE);
        return ResponseEntity.ok("Todo item with id " + updatedId.toString() + " marked as done!");
    }

    @PatchMapping("/pending")
    public ResponseEntity<String> todoItemPending() {
        Long updatedId = todoItemService.updateStatus(this.todoItem, TodoItemStatus.PENDING);
        return ResponseEntity.ok("Todo item with id " + updatedId.toString() + " marked as pending!");
    }

    @PatchMapping("/update")
    public ResponseEntity<String> todoItemUpdate(@RequestBody TodoItemUpdateDto todoItemUpdateDto) {
        Long updatedId = todoItemService.update(this.todoItem, todoItemUpdateDto);
        return ResponseEntity.ok("Todo item with id " + updatedId.toString() + " marked as updated!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> todoItemDelete(@PathVariable("id") Long id) {
        todoItemService.deleteTodoItem(todoItem);
        return ResponseEntity.noContent().build();
    }
}
