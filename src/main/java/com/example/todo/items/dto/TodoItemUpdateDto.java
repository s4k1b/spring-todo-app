package com.example.todo.items.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TodoItemUpdateDto {
    private String title;
}
