package ru.parfenov.server.pages.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.server.model.User;
import ru.parfenov.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ResetPasswordPageTest {

    @Test
    @DisplayName("Юзер ввёл корректный код сброса пароля")
    public void test_correct_reset_password_code() throws IOException,
            InterruptedException {
        User user = mock(User.class);
        UserService userService = mock(UserService.class);
        when(user.getResetPassword()).thenReturn("correctCode");
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("correctCode");
        ResetPasswordPage page = new ResetPasswordPage(user, userService);
        page.reader = reader;
        page.run();
        verify(userService, times(1)).update(anyInt(), anyString(),
                anyString(), eq(""), isNull(), eq(""));
    }

    @Test
    @DisplayName("Ввод нового пароля после сброса")
    public void test_new_password_after_correct_code() throws
            IOException, InterruptedException {
        User user = mock(User.class);
        UserService userService = mock(UserService.class);
        when(user.getResetPassword()).thenReturn("correctCode");
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("correctCode", "newPassword");
        ResetPasswordPage page = new ResetPasswordPage(user, userService);
        page.reader = reader;
        page.run();
        verify(userService).update(anyInt(), eq("newPassword"),
                anyString(), eq(""), isNull(), eq(""));
    }

    @Test
    @DisplayName("Успешное обновление данных юзера")
    public void test_user_service_update_success() throws IOException,
            InterruptedException {
        User user = mock(User.class);
        UserService userService = mock(UserService.class);
        when(user.getResetPassword()).thenReturn("correctCode");
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("correctCode", "newPassword");
        ResetPasswordPage page = new ResetPasswordPage(user, userService);
        page.reader = reader;
        page.run();
        verify(userService, times(1)).update(anyInt(),
                eq("newPassword"), anyString(), eq(""), isNull(), eq(""));
    }

    @Test
    @DisplayName("Юзер ввёл не корректный код сброса пароля")
    public void test_incorrect_reset_password_code() throws
            IOException, InterruptedException {
        User user = mock(User.class);
        UserService userService = mock(UserService.class);
        when(user.getResetPassword()).thenReturn("correctCode");
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("wrongCode");
        ResetPasswordPage page = new ResetPasswordPage(user, userService);
        page.reader = reader;
        page.run();
        verify(userService, never()).update(anyInt(), anyString(),
                anyString(), anyString(), any(), anyString());
    }

    @Test
    @DisplayName("Юзер ввёл пустую строку вместо кода сброса пароля")
    public void test_empty_reset_password_code() throws IOException,
            InterruptedException {
        User user = mock(User.class);
        UserService userService = mock(UserService.class);
        when(user.getResetPassword()).thenReturn("correctCode");
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("");
        ResetPasswordPage page = new ResetPasswordPage(user, userService);
        page.reader = reader;
        page.run();
        verify(userService, never()).update(anyInt(), anyString(),
                anyString(), anyString(), any(), anyString());
    }

    @Test
    @DisplayName("Юзер ввёл пустую строку вместо нового пароля")
    public void test_empty_new_password() throws IOException,
            InterruptedException {
        User user = mock(User.class);
        UserService userService = mock(UserService.class);
        when(user.getResetPassword()).thenReturn("correctCode");
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("correctCode", "");
        ResetPasswordPage page = new ResetPasswordPage(user, userService);
        page.reader = reader;
        page.run();
        verify(userService).update(anyInt(), eq(""), anyString(),
                eq(""), isNull(), eq(""));
    }

    // IOException occurs during input reading
    @Test
    @DisplayName("IOException")
    public void test_io_exception_during_input_reading() throws
            IOException {
        User user = mock(User.class);
        UserService userService = mock(UserService.class);
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenThrow(new IOException());
        ResetPasswordPage page = new ResetPasswordPage(user, userService);
        page.reader = reader;

        assertThrows(IOException.class, page::run);

        verify(userService, never()).update(anyInt(), anyString(),
                anyString(), anyString(), any(), anyString());
    }
}