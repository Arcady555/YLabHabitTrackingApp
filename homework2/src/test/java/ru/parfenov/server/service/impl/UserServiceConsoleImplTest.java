package ru.parfenov.server.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.server.enums.user.Role;
import ru.parfenov.server.model.User;
import ru.parfenov.server.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserServiceConsoleImplTest {

    @Test
    @DisplayName("Успешное создание юзера")
    public void test_create_user_success() {
        UserRepository userRepository = mock(UserRepository.class);
        UserServiceConsoleImpl userService = new
                UserServiceConsoleImpl(userRepository);
        User user = new User(0, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        when(userRepository.create(any(User.class))).thenReturn(user);

        User createdUser = userService.createByReg("test@example.com",
                "password", "Test User");

        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("Test User", createdUser.getName());
    }

    @Test
    @DisplayName("Найти существующего юзера по емайл")
    public void test_find_user_by_email_success() {
        UserRepository userRepository = mock(UserRepository.class);
        UserServiceConsoleImpl userService = new
                UserServiceConsoleImpl(userRepository);
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        Optional<User> foundUser = userService.findByEmail("test@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Найти юзера по ID")
    public void test_find_user_by_id_success() {
        UserRepository userRepository = mock(UserRepository.class);
        UserServiceConsoleImpl userService = new
                UserServiceConsoleImpl(userRepository);
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        when(userRepository.findById(1)).thenReturn(user);

        Optional<User> foundUser = userService.findById(1);

        assertTrue(foundUser.isPresent());
        assertEquals(1, foundUser.get().getId());
    }

    @Test
    @DisplayName("Найти юзера по емайл и паролю")
    public void test_find_user_by_email_and_password_success() {
        UserRepository userRepository = mock(UserRepository.class);
        UserServiceConsoleImpl userService = new
                UserServiceConsoleImpl(userRepository);
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        when(userRepository.findByEmailAndPassword("test@example.com",
                "password")).thenReturn(user);

        Optional<User> foundUser =
                userService.findByEmailAndPassword("test@example.com", "password");

        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Создать юзера с не уникальным емайл")
    public void test_create_user_with_existing_email() {
        UserRepository userRepository = mock(UserRepository.class);
        UserServiceConsoleImpl userService = new
                UserServiceConsoleImpl(userRepository);
        when(userRepository.create(any(User.class))).thenThrow(new
                IllegalArgumentException("Duplicate email"));

        Exception exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    userService.createByReg("duplicate@example.com",
                            "password", "Duplicate User");
                });

        assertEquals("Duplicate email", exception.getMessage());
    }

    @Test
    @DisplayName("Поиск юзера по несуществующему емайл")
    public void test_find_user_by_non_existing_email() {
        UserRepository userRepository = mock(UserRepository.class);
        UserServiceConsoleImpl userService = new
                UserServiceConsoleImpl(userRepository);
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        Optional<User> foundUser =
                userService.findByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Поиск юзера по несуществующему ID")
    public void test_find_user_by_non_existing_id() {
        UserRepository mockStore = mock(UserRepository.class);
        UserServiceConsoleImpl userService = new
                UserServiceConsoleImpl(mockStore);
        when(mockStore.findById(999)).thenReturn(null);

        Optional<User> foundUser = userService.findById(999);

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Поиск юзера по неправильному паролю")
    public void test_find_user_with_incorrect_password() {
        UserRepository userRepository = mock(UserRepository.class);
        UserServiceConsoleImpl userService = new
                UserServiceConsoleImpl(userRepository);
        when(userRepository.findByEmailAndPassword("test@example.com",
                "wrongpassword")).thenReturn(null);

        Optional<User> foundUser =
                userService.findByEmailAndPassword("test@example.com",
                        "wrongpassword");

        assertFalse(foundUser.isPresent());
    }
}