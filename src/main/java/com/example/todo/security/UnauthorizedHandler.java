package com.example.todo.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UnauthorizedHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        String exceptionType = (String) request.getAttribute("exception");
        System.out.println(authException.getMessage());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorJson;
        if ("TokenExpired".equals(exceptionType)) {
            errorJson = "{\"error\": \"Unauthorized\", \"message\": \"Token has expired\"}";
        } else if ("InvalidToken".equals(exceptionType)) {
            errorJson = "{\"error\": \"Unauthorized\", \"message\": \"Invalid token\"}";
        } else {
            errorJson = "{\"error\": \"Unauthorized\", \"message\": \"Authentication required\"}";
        }

        response.getWriter().write(errorJson);
    }
}
