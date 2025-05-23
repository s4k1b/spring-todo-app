package com.example.todo.exeptions;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidError(MethodArgumentNotValidException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDate.now());
        response.put("message", e.getBindingResult().getAllErrors().stream().map((error) -> error.getDefaultMessage()).collect(Collectors.joining(", ")));
        response.put("cause", e.getCause());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Map<String, Object>> handleRuntimeError(RuntimeException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDate.now());
        response.put("message", e.getMessage());
        response.put("cause", e.getCause());

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEnum(MethodArgumentTypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid arguments: " + ex.getName() + ": " + ex.getValue());
        return ResponseEntity.badRequest().body(error);
    }
}
