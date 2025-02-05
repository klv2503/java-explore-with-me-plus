package ru.practicum.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserToDtoMapper {

    User mapUserDtoToUser(UserDto userDto);

    UserDto mapUserToUserDto(User user);

    //List<User> mapDtoListToUsersList(List<UserDto> userDtos);

    List<UserDto> mapUsersListToDtoList(List<User> users);

}
