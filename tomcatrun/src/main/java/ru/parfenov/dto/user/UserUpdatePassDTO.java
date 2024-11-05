package ru.parfenov.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для удобной подачи в json. Оставлены только поля, нужные для сброса пароля
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserUpdatePassDTO {
    private String password;
    private String resetPassword;
}
