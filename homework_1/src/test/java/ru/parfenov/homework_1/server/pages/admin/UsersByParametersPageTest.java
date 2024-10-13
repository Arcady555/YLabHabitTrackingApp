package ru.parfenov.homework_1.server.pages.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

public class UsersByParametersPageTest {
    UserService mockService = mock(UserService.class);
    UsersByParametersPage page = new UsersByParametersPage(mockService);
    BufferedReader mockReader = mock(BufferedReader.class);

    @Test
    @DisplayName("Поиск по роли")
    public void test_correct_role_input() throws IOException {
        page.reader = mockReader;

        when(mockReader.readLine()).thenReturn("0", "", "");
        List<User> expectedUsers = List.of(new User(1,
                "admin@example.com", "pass", "Admin", Role.ADMIN, false));
        when(mockService.findByParameters("ADMIN", "", "")).thenReturn(expectedUsers);

        page.run();

        verify(mockService).findByParameters("ADMIN", "", "");
    }

    @Test
    @DisplayName("Поиск по имени")
    public void test_correct_name_input() throws IOException {
        page.reader = mockReader;

        when(mockReader.readLine()).thenReturn("", "John", "");
        List<User> expectedUsers = List.of(new User(2,
                "john@example.com", "pass", "John", Role.CLIENT, false));
        when(mockService.findByParameters(null, "John", "")).thenReturn(expectedUsers);

        page.run();

        verify(mockService).findByParameters(null, "John", "");
    }

    @Test
    @DisplayName("Поиск по статусу")
    public void test_correct_block_status_input() throws IOException {
        page.reader = mockReader;

        when(mockReader.readLine()).thenReturn("", "", "0");
        List<User> expectedUsers = List.of(new User(3,
                "blocked@example.com", "pass", "BlockedUser", Role.CLIENT, true));
        when(mockService.findByParameters(null, "", "true")).thenReturn(expectedUsers);

        page.run();

        verify(mockService).findByParameters(null, "", "true");
    }

    @Test
    @DisplayName("Поиск по имени и блоку")
    public void test_empty_name_input() throws IOException {
        page.reader = mockReader;

        when(mockReader.readLine()).thenReturn("0", "", "1");
        List<User> expectedUsers = List.of(
                new User(5, "admin1@example.com", "pass", "Admin1",
                        Role.ADMIN, false),
                new User(6, "admin2@example.com", "pass", "Admin2",
                        Role.ADMIN, false)
        );
        when(mockService.findByParameters("ADMIN", "", "false")).thenReturn(expectedUsers);

        page.run();

        verify(mockService).findByParameters("ADMIN", "", "false");
    }

    @Test
    @DisplayName("Поиск по всеми 3м параметрам")
    public void test_empty_habit_input() throws IOException {
        page.reader = mockReader;

        when(mockReader.readLine()).thenReturn("1", "John", "0");
        List<User> expectedUsers = List.of(
                new User(7, "john1@example.com", "pass", "John1",
                        Role.CLIENT, true),
                new User(8, "john2@example.com", "pass", "John2", Role.CLIENT, true)
        );
        when(mockService.findByParameters("CLIENT", "John", "true")).thenReturn(expectedUsers);

        page.run();

        verify(mockService).findByParameters("CLIENT", "John", "true");
    }
}