package ru.parfenov.dto.user;

import org.mapstruct.Mapper;
import ru.parfenov.homework_3.model.User;

@Mapper
public interface UserDTOMapper {
    UserNamePasContDTO toUserDtoFoReg(User source);

    User toUser(UserNamePasContDTO destination);

    UserAllParamDTO toUserAllParamDTO(User source);

    User toUser(UserAllParamDTO destination);

    UserIdNameRoleDTO toUserIdNameRoleDTO(User source);

    User toUser(UserIdNameRoleDTO destination);

    UserIdPassDTO toUserIdPassDTO(User source);

    User toUser(UserIdPassDTO destination);
}
