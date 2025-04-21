package com.example.todo.items.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TodoItemCreateDto {
    @NotBlank(message = "Todo title is required")
    private String title;
}
