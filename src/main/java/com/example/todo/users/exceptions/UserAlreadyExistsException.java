package com.example.todo.users.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
