package ru.parfenov.dto.user;

import lombok.Data;

/**
 * DTO для удобной подачи в json. Enum заменили на String
 * И оставлены только поля, нужные для ввода при sign-in
 */
@Data
public class UserIdPassDTO {
    private int id;
    private String password;
}
