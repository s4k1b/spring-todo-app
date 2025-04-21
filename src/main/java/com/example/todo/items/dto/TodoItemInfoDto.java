package com.example.todo.items.dto;

import com.example.todo.items.enums.TodoItemStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TodoItemInfoDto {
    private Long id;
    private String title;
    private TodoItemStatus status;
    private LocalDate createdAt;
}
