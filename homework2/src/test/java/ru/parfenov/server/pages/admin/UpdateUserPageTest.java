package ru.parfenov.server.pages.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.server.enums.user.Role;
import ru.parfenov.server.model.User;
import ru.parfenov.server.service.HabitService;
import ru.parfenov.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UpdateUserPageTest {

    @Test
    @DisplayName("Юзер найден по емайл")
    public void test_user_found_by_email() throws IOException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        UpdateUserPage updateUserPage = new
                UpdateUserPage(userService, habitService);
        User user = new User(1, "test@example.com", "password", "",
                "Test User", Role.CLIENT, false);
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("test@example.com", "1");
        updateUserPage.reader = reader;
        updateUserPage.run();
        verify(userService).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Юзер успешно удалён")
    public void test_user_deleted_when_confirmed() throws IOException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        UpdateUserPage updateUserPage = new
                UpdateUserPage(userService, habitService);
        User user = new User(1, "test@example.com", "password", "",
                "Test User", Role.CLIENT, false);
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userService.delete(user)).thenReturn(true);
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("test@example.com", "0");
        updateUserPage.reader = reader;
        updateUserPage.run();
        verify(habitService).deleteWithUser(user);
        verify(userService).delete(user);
    }

    @Test
    @DisplayName("Юзер не удалён из-за отказа сервиса")
    public void test_user_deletion_fails() throws IOException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        UpdateUserPage updateUserPage = new
                UpdateUserPage(userService, habitService);
        User user = new User(1, "test@example.com", "password", "",
                "Test User", Role.CLIENT, false);
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userService.delete(user)).thenReturn(false);
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("test@example.com", "0");
        updateUserPage.reader = reader;
        updateUserPage.run();
        verify(habitService).deleteWithUser(user);
        verify(userService).delete(user);
    }

    @Test
    @DisplayName("Юзер не обновлён из-за неправильных данных")
    public void test_user_update_fails_due_to_invalid_inputs() throws
            IOException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        UpdateUserPage updateUserPage = new
                UpdateUserPage(userService, habitService);
        User user = new User(1, "test@example.com", "password", "",
                "Test User", Role.CLIENT, false);
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("test@example.com", "1",
                "1", "1", "");
        updateUserPage.reader = reader;
        updateUserPage.run();
        verify(userService).update(eq(user.getId()), anyString(),
                anyString(), anyString(), any(), anyString());
    }
}