package ru.parfenov.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO для удобной подачи в json. Оставлены только поля, нужные для ввода при обновлении данных юзера
 */
@Data
@AllArgsConstructor
public class UserUpdateDTO {
    private int id;
    private String password;
    private String name;
    private String role;
    private String blocked;
}
