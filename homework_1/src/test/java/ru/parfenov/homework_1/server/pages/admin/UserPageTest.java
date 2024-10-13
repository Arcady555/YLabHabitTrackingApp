package ru.parfenov.homework_1.server.pages.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserPageTest {

    @Test
    @DisplayName("Успешное отображение юзера при вводе валидного емайла")
    public void test_retrieve_user_details_with_valid_email() throws
            IOException {
        UserService mockService = mock(UserService.class);
        UserPage userPage = new UserPage(mockService);
        BufferedReader mockReader = mock(BufferedReader.class);
        userPage.reader = mockReader;

        when(mockReader.readLine()).thenReturn("test@example.com");

        User user = new User(1, "test@example.com", "password", "Test User", null, false);
        when(mockReader.readLine()).thenReturn("test@example.com");
        when(mockService.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        userPage.run();

        verify(mockService).findByEmail("test@example.com");
        assertEquals("User{id=1, email='test@example.com', password='password', name='Test User', role=null, blocked=false}",
                user.toString());
    }

    @Test
    @DisplayName("Ввод не валидного емайла")
    public void test_display_message_for_invalid_email() throws IOException {
        UserService mockService = mock(UserService.class);
        UserPage userPage = new UserPage(mockService);
        BufferedReader mockReader = mock(BufferedReader.class);
        userPage.reader = mockReader;

        when(mockReader.readLine()).thenReturn("invalid@example.com");

        when(mockService.findByEmail("invalid@example.com")).thenReturn(Optional.empty());

        userPage.run();

        verify(mockService).findByEmail("invalid@example.com");
    }

    @Test
    @DisplayName("Отсутствие исключений при вводе валидного емайла")
    public void test_execution_without_exceptions() throws IOException {
        UserService mockService = mock(UserService.class);
        UserPage userPage = new UserPage(mockService);
        BufferedReader mockReader = mock(BufferedReader.class);
        userPage.reader = mockReader;

        when(mockReader.readLine()).thenReturn("test@example.com");

        when(mockReader.readLine()).thenReturn("test@example.com");
        when(mockService.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertDoesNotThrow(userPage::run);
    }

    @Test
    @DisplayName("Правильная инициализация UserPage с экземпляром UserService")
    public void test_initialization_with_user_service() {
        UserService mockService = mock(UserService.class);
        UserPage userPage = new UserPage(mockService);
        BufferedReader mockReader = mock(BufferedReader.class);
        assertNotNull(userPage);
    }

    @Test
    @DisplayName("Проброс IO Exception")
    public void test_handle_io_exception_during_input() throws IOException {
        UserService mockService = mock(UserService.class);
        UserPage userPage = new UserPage(mockService);
        BufferedReader mockReader = mock(BufferedReader.class);
        userPage.reader = mockReader;

        when(mockReader.readLine()).thenReturn("test@example.com");

        when(mockReader.readLine()).thenThrow(new IOException("IO Exception"));

        assertThrows(IOException.class, userPage::run);
    }

    @Test
    @DisplayName("Если сервис вернул empty Optional")
    public void test_handle_empty_optional_from_user_service() throws
            IOException {
        UserService mockService = mock(UserService.class);
        UserPage userPage = new UserPage(mockService);
        BufferedReader mockReader = mock(BufferedReader.class);
        userPage.reader = mockReader;

        when(mockReader.readLine()).thenReturn("unknown@example.com");

        when(mockReader.readLine()).thenReturn("unknown@example.com");
        when(mockService.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        userPage.run();

        verify(mockService).findByEmail("unknown@example.com");
    }
}