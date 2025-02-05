package ru.practicum.users.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.users.dto.NewUserRequest;
import ru.practicum.users.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface NewUserRequestToUserMapper {

    User mapNewUserRequestToUser(NewUserRequest newUserRequest);

}
