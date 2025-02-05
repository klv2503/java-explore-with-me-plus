package ru.practicum.users.service;

import ru.practicum.users.dto.GetUsersDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.NewUserRequest;

import java.util.List;

public interface AdminUserService {

    List<UserDto> getUsers(GetUsersDto parameters);

    UserDto addUser(NewUserRequest user);

    void deleteUser(Long id);
}
