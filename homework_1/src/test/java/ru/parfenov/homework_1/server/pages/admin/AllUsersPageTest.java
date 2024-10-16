package ru.parfenov.homework_1.server.pages.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.UserService;

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
}