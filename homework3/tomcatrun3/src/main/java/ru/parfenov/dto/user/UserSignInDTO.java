package ru.parfenov.dto.user;

import lombok.Data;

/**
 * DTO для удобной подачи в json. Оставлены только поля, нужные для ввода при sign-in
 */
@Data
public class UserSignInDTO {
    private String email;
    private String password;
}