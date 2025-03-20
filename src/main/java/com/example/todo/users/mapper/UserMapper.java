package com.example.todo.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.todo.users.dto.UserInfoDto;
import com.example.todo.users.dto.UserRegistrationDto;
import com.example.todo.users.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserInfoDto userEntityToDto(User user);

    @Mapping(target = "id", ignore = true)
    User userRegistrationDtoToEntity(UserRegistrationDto userDto);
}
