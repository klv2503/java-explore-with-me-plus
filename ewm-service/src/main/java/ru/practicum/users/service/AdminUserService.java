package ru.practicum.users.service;

import ru.practicum.users.dto.GetUsersDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserShortDto;

import java.util.List;

public interface AdminUserService {

    List<UserDto> getUsers(GetUsersDto parameters);

    UserDto addUser(UserShortDto user);

    void deleteUser(Long id);
}
