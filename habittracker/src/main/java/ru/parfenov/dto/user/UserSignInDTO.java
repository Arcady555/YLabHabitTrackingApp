package ru.parfenov.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для удобной подачи в json. Оставлены только поля, нужные входа в приложение зарегистрированным юзерам
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSignInDTO {
    private String email;
    private String password;
}