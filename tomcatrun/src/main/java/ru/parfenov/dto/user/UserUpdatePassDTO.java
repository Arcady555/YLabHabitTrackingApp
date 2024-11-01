package ru.parfenov.dto.user;

import lombok.Data;

/**
 * DTO для удобной подачи в json. Оставлены только поля, нужные для сброса пароля
 */
@Data
public class UserUpdatePassDTO {
    private String password;
    private String resetPassword;
}
