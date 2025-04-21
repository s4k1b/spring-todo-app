package com.example.todo.items.mapper;

import com.example.todo.items.dto.TodoItemCreateDto;
import com.example.todo.items.dto.TodoItemInfoDto;
import com.example.todo.items.entity.TodoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TodoItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    TodoItem todoItemCreateDtoToEntity(TodoItemCreateDto todoItemCreateDto);

    TodoItemInfoDto todoItemEntityToInfo(TodoItem todoItem);
}
