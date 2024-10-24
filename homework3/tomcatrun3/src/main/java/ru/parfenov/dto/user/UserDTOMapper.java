package ru.parfenov.dto.user;

import org.mapstruct.Mapper;
import ru.parfenov.model.User;

@Mapper
public interface UserDTOMapper {
    UserSignUpDTO toUserDtoFoReg(User source);

    User toUser(UserSignUpDTO destination);

    UserAllParamDTO toUserAllParamDTO(User source);

    User toUser(UserAllParamDTO destination);

    UserIdNameRoleDTO toUserIdNameRoleDTO(User source);

    User toUser(UserIdNameRoleDTO destination);

    UserSignInDTO toUserIdPassDTO(User source);

    User toUser(UserSignInDTO destination);
}
