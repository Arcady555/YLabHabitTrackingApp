package ru.parfenov.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для удобной подачи в json. Оставлены только поля, нужные для ввода при обновлении данных юзера
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {
    private int id;
    private String password;
    private String name;
    private String role;
    private String blocked;
}
