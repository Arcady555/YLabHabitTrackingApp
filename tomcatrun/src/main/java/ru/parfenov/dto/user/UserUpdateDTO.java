package ru.parfenov.dto.user;

import lombok.*;

/**
 * DTO для удобной подачи в json. Оставлены только поля, нужные для ввода при обновлении данных юзера
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserUpdateDTO {
    private int id;
    private String password;
    private String name;
    private String role;
    private String blocked;
}
