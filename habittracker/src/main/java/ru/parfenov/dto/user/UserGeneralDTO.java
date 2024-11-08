package ru.parfenov.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.parfenov.enums.user.Role;

/**
 * DTO для удобной подачи в json. Оставлены только поля, нужные для вывода юзера с данными(без пароля и резетпароля)
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserGeneralDTO {
    private int id;
    private String email;
    private String name;
    private Role role;
    private boolean blocked;
}
