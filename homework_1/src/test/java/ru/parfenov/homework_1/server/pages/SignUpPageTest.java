package ru.parfenov.homework_1.server.pages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SignUpPageTest {
    UserService mockService = mock(UserService.class);
    SignUpPage signUpPage = new SignUpPage(mockService);
    BufferedReader mockReader = mock(BufferedReader.class);

    @Test
    @DisplayName("Успешная регистрация юзера")
    public void test_user_creation_with_valid_input() throws IOException {
        signUpPage.reader = mockReader;

        when(mockReader.readLine()).thenReturn("John Doe",
                "john.doe@example.com", "password123");
        when(mockService.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(mockService.createByReg("john.doe@example.com",
                "password123", "John Doe")).thenReturn(new User());

        signUpPage.run();

        verify(mockService).createByReg("john.doe@example.com",
                "password123", "John Doe");
    }

    @Test
    @DisplayName("Регистрация с дефолтным именем")
    public void test_default_name_assignment() throws IOException {
        signUpPage.reader = mockReader;

        when(mockReader.readLine()).thenReturn("",
                "john.doe@example.com", "password123");
        when(mockService.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(mockService.createByReg("john.doe@example.com",
                "password123", "no name")).thenReturn(new User());

        signUpPage.run();

        verify(mockService).createByReg("john.doe@example.com",
                "password123", "no name");
    }

    @Test
    @DisplayName("Выброс IOException")
    public void test_io_exceptions_during_input() throws IOException {
        signUpPage.reader = mockReader;

        when(mockReader.readLine()).thenThrow(new IOException("IO Exception"));

        assertThrows(IOException.class, signUpPage::run);

        verify(mockService, never()).createByReg(anyString(),
                anyString(), anyString());
    }
}