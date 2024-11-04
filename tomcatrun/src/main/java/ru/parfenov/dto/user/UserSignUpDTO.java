package ru.parfenov.dto.user;

import lombok.*;

/**
 * DTO для удобной подачи в json. Enum заменили на String
 * И оставлены только поля, нужные для ввода при sign-up
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSignUpDTO {
    private String email;
    private String password;
    private String name;
}
