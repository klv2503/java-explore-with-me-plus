package ru.practicum.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.users.dto.UserShortDto;
import ru.practicum.users.model.User;

//import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface UserToShortDtoMapper {

    User mapUserShortDtoToUser(UserShortDto userDto);

    //UserShortDto mapUserToUserShortDto(User user);

    //List<User> mapShortDtoListToUsersList(List<UserShortDto> userDtos);

    //List<UserShortDto> mapUsersListToShortDtoList(List<User> users);

}
