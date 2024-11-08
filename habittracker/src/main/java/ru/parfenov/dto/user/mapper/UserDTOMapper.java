package ru.parfenov.dto.user.mapper;

import org.mapstruct.Mapper;
import ru.parfenov.dto.user.UserGeneralDTO;
import ru.parfenov.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    /**
     * Перевод User сущности в DTO, со всеми исходными полями, кроме пароля и резетпароля.
     * Применяется при распечатке данных юзера
     *
     * @param source User - сущность из блока ru/parfenov/model
     * @return DTO объект
     */
    UserGeneralDTO toUserGeneralDTO(User source);

    /**
     * Перевод списка с User в список с UserGeneralDTO.
     * Применяется при распечатке данных
     *
     * @param source список сущностей User
     * @return список объектов DTO
     */
    List<UserGeneralDTO> toUserGeneralDTOList(List<User> source);
}