package ru.parfenov.server.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.server.enums.user.Role;
import ru.parfenov.server.model.User;
import ru.parfenov.server.service.HabitService;
import ru.parfenov.server.service.UserService;

import java.io.BufferedReader;
import java.util.Optional;

import static org.mockito.Mockito.*;

class SignInPageTest {

    @Test
    @DisplayName("Юзер ввёл емайл некорректного формата")
    public void test_incorrect_email_format() throws Exception {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);

        SignInPage signInPage = new SignInPage(userService, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        signInPage.reader = reader;

        when(reader.readLine()).thenReturn("invalid-email-format");

        signInPage.run();

        verify(userService).findByEmail("invalid-email-format");
    }

    @Test
    @DisplayName("Юзер ввёл правильный емайл и неправильный пароль")
    public void test_correct_email_incorrect_password() throws Exception {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        User user = new User(1, "test@example.com", "password",
                "resetCode", "Test User", Role.CLIENT, false);
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        SignInPage signInPage = new SignInPage(userService, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        signInPage.reader = reader;

        when(reader.readLine()).thenReturn("test@example.com", "wrongpassword");

        signInPage.run();

        verify(userService).findByEmail("test@example.com");
        verify(userService).findByEmailAndPassword("test@example.com",
                "wrongpassword");
    }

    @Test
    @DisplayName("Юзер ввёл несуществующий емайл")
    public void test_non_existent_email() throws Exception {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);

        SignInPage signInPage = new SignInPage(userService, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        signInPage.reader = reader;

        when(reader.readLine()).thenReturn("nonexistent@example.com");

        signInPage.run();

        verify(userService).findByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Заблокированный юзер пытается войти")
    public void test_login_while_blocked() throws Exception {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        User blockedUser = new User(1, "blocked@example.com",
                "password", "resetCode", "Blocked User", Role.CLIENT, true);
        when(userService.findByEmail("blocked@example.com")).thenReturn(Optional.of(blockedUser));

        SignInPage signInPage = new SignInPage(userService, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        signInPage.reader = reader;

        when(reader.readLine()).thenReturn("blocked@example.com", "password");

        signInPage.run();

        verify(userService).findByEmail("blocked@example.com");
    }
}