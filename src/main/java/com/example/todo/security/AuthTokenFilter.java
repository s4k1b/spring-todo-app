package com.example.todo.security;

import java.io.IOException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.todo.users.entity.User;
import com.example.todo.users.mapper.UserMapper;
import com.example.todo.users.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final UserMapper userMapper;

    public AuthTokenFilter(JwtUtils jwtUtils, UserService userService, UserMapper userMapper) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException , BadCredentialsException {
        try {
            String token = extractTokenFromHeader(request);
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String userEmail = jwtUtils.extractUserEmail(token);
                if (userEmail != null) {
                    User user = userService.findUserByEmail(userEmail);
                    if (jwtUtils.isTokenValid(token, userMapper.userEntityToDto(user))) {
                        UsernamePasswordAuthenticationToken userAuthToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        userAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(userAuthToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch(ExpiredJwtException e) {
            request.setAttribute("exception", "TokenExpired");
            throw new BadCredentialsException("Token expired", e);
        } catch(JwtException e) {
            request.setAttribute("exception", "InvalidToken");
            throw new BadCredentialsException("Invalid Token", e);
        }

    }
}
