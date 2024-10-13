package ru.parfenov.homework_1.server.pages.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.HabitService;
import ru.parfenov.homework_1.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UpdateUserPageTest {
    UserService service;
    HabitService habitService;
    UpdateUserPage updateUserPage;
    BufferedReader reader;

    @BeforeEach
    public void init() {
        service = mock(UserService.class);
        habitService = mock(HabitService.class);
        updateUserPage = new UpdateUserPage(service, habitService);
        reader = mock(BufferedReader.class);
    }

    @Test
    @DisplayName("Изменение блока юзера")
    public void test_block_user_confirmation() throws IOException,
            InterruptedException {
        User user = new User(1, "test@example.com", "password", "Test User", Role.CLIENT, false);

        when(service.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(reader.readLine()).thenReturn("test@example.com", "1", "0");

        updateUserPage.reader = reader;
        updateUserPage.run();

        assertTrue(user.isBlocked());
    }

    @Test
    @DisplayName("Изменение роли юзера")
    public void test_change_user_role_to_admin() throws IOException,
            InterruptedException {
        User user = new User(1, "test@example.com", "password", "Test User", Role.CLIENT, false);

        when(service.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(reader.readLine()).thenReturn("test@example.com", "1", "1", "0");

        updateUserPage.reader = reader;
        updateUserPage.run();

        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    @DisplayName("Неправильный ввод")
    public void test_handle_invalid_input() throws IOException,
            InterruptedException {
        User user = new User(1, "test@example.com", "password", "Test User", Role.CLIENT, false);

        when(service.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(reader.readLine()).thenReturn("test@example.com", "invalid");

        updateUserPage.reader = reader;
        updateUserPage.run();

        assertFalse(user.isBlocked());
    }

    @Test
    @DisplayName("Ничего не изменили")
    public void test_no_changes_made_to_user() throws IOException,
            InterruptedException {
        User user = new User(1, "test@example.com", "password", "Test User", Role.CLIENT, false);

        when(service.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(reader.readLine()).thenReturn("test@example.com", "1",
                "1", "1", "1");

        updateUserPage.reader = reader;
        updateUserPage.run();

        assertEquals(Role.CLIENT, user.getRole());
        assertFalse(user.isBlocked());
    }

    @Test
    @DisplayName("Проброс IOException")
    public void test_handle_io_exception() throws IOException {

        when(reader.readLine()).thenThrow(new IOException());

        updateUserPage.reader = reader;

        assertThrows(IOException.class, updateUserPage::run);
    }
}