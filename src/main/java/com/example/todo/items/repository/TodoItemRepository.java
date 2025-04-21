package com.example.todo.items.repository;

import com.example.todo.items.entity.TodoItem;
import com.example.todo.items.enums.TodoItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {
    TodoItem findById(long id);
    TodoItem findByStatus(TodoItemStatus status);
}
