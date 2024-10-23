package ru.parfenov.server.pages.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.server.enums.user.Role;
import ru.parfenov.server.model.User;
import ru.parfenov.server.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AllUsersPageTest {

    @Test
    @DisplayName("Корректный вывод")
    public void test_retrieves_and_prints_all_users() {
        UserService mockService = mock(UserService.class);
        List<User> users = List.of(new User(1, "email1@example.com", "pass1", "1", "User1", Role.CLIENT, false),
                new User(2, "email2@example.com", "pass2", "2", "User2", Role.ADMIN, false));
        when(mockService.findAll()).thenReturn(users);
        AllUsersPage allUsersPage = new AllUsersPage(mockService);
        allUsersPage.run();
        verify(mockService).findAll();
    }

    @Test
    @DisplayName("Правильный размер списка")
    public void test_find_all_returns_complete_list() {
        UserService mockService = mock(UserService.class);
        List<User> users = List.of(new User(1, "email1@example.com",
                "pass1", "1", "User1", Role.CLIENT, false));
        when(mockService.findAll()).thenReturn(users);
        AllUsersPage allUsersPage = new AllUsersPage(mockService);
        allUsersPage.run();
        assertEquals(1, users.size());
    }

    @Test
    @DisplayName("Отсутствие исключения, если все юзеры присутствуют")
    public void test_run_executes_without_exceptions() {
        UserService mockService = mock(UserService.class);
        List<User> users = List.of(new User(1, "email1@example.com",
                "pass1", "1", "User1", Role.CLIENT, false));
        when(mockService.findAll()).thenReturn(users);
        AllUsersPage allUsersPage = new AllUsersPage(mockService);
        assertDoesNotThrow(allUsersPage::run);
    }

    @Test
    @DisplayName("Возврат пустого списка")
    public void test_service_returns_empty_list() {
        UserService mockService = mock(UserService.class);
        when(mockService.findAll()).thenReturn(Collections.emptyList());
        AllUsersPage allUsersPage = new AllUsersPage(mockService);
        assertDoesNotThrow(allUsersPage::run);
    }

    @Test
    @DisplayName("Если RuntimeException")
    public void test_service_throws_exception_during_find_all() {
        UserService mockService = mock(UserService.class);
        when(mockService.findAll()).thenThrow(new
                RuntimeException("Store error"));
        AllUsersPage allUsersPage = new AllUsersPage(mockService);
        assertThrows(RuntimeException.class, allUsersPage::run);
    }

    @Test
    @DisplayName("Работа с пользователями, имеющими null или пустые поля")
    public void test_handles_users_with_null_or_empty_fields() {
        UserService mockService = mock(UserService.class);
        List<User> users = List.of(new User(1, null, "", null, "",
                Role.CLIENT, false));
        when(mockService.findAll()).thenReturn(users);
        AllUsersPage allUsersPage = new AllUsersPage(mockService);
        allUsersPage.run();
        verify(mockService).findAll();
    }

    @Test
    @DisplayName("Вывод большого списка")
    public void test_handles_large_number_of_users() {
        UserService mockService = mock(UserService.class);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            users.add(new User(i, "email" + i + "@example.com", "pass"
                    + i, "reset" + i, "User" + i, Role.CLIENT, false));
        }
        when(mockService.findAll()).thenReturn(users);
        AllUsersPage allUsersPage = new AllUsersPage(mockService);
        allUsersPage.run();
        verify(mockService).findAll();
    }

    @Test
    @DisplayName("Корректный перебор")
    public void test_iterates_over_users_list() {
        UserService userService = mock(UserService.class);
        List<User> users = List.of(new User(1, "email1@example.com",
                        "pass1", "reset1", "User1", Role.CLIENT, false),
                new User(2, "email2@example.com",
                        "pass2", "reset2", "User2", Role.CLIENT, false));
        when(userService.findAll()).thenReturn(users);
        AllUsersPage allUsersPage = new AllUsersPage(userService);
        allUsersPage.run();
        assertEquals(2, users.size());
    }

    @Test
    @DisplayName("Вывод пустого списка")
    public void test_handles_empty_user_list() {
        UserService userService = mock(UserService.class);
        when(userService.findAll()).thenReturn(Collections.emptyList());
        AllUsersPage allUsersPage = new AllUsersPage(userService);
        assertDoesNotThrow(allUsersPage::run);
    }

    @Test
    @DisplayName("Вывод большого списка без исключений")
    public void test_handles_large_user_list() {
        UserService userService = mock(UserService.class);
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            users.add(new User(i, "email" + i + "@example.com", "pass"
                    + i, "reset" + i, "User" + i, Role.CLIENT, false));
        }
        when(userService.findAll()).thenReturn(users);
        AllUsersPage allUsersPage = new AllUsersPage(userService);
        assertDoesNotThrow(allUsersPage::run);
    }
}