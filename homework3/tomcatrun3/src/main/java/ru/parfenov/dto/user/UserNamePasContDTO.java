package ru.parfenov.dto.user;

import lombok.Data;

/**
 * DTO для удобной подачи в json. Enum заменили на String
 * И оставлены только поля, нужные для ввода при sign-up
 */
@Data
public class UserNamePasContDTO {
    private String name;
    private String password;
    private String contactInfo;
}
