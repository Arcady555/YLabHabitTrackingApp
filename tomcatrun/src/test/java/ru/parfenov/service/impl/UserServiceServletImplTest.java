package ru.parfenov.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserServiceServletImplTest {

    @Test
    @DisplayName("Успешное создание юзера")
    public void test_create_user_success() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new
                UserServiceServletImpl(userRepository);
        User user = new User(0, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        when(userRepository.create(any(User.class))).thenReturn(user);

        User createdUser = userService.createByReg("test@example.com",
                "password", "Test User").get();

        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("Test User", createdUser.getName());
    }

    @Test
    @DisplayName("Найти существующего юзера по емайл")
    public void test_find_user_by_email_success() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new
                UserServiceServletImpl(userRepository);
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
        UserService userService = new
                UserServiceServletImpl(userRepository);
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
        UserService userService = new
                UserServiceServletImpl(userRepository);
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
        UserService userService = new
                UserServiceServletImpl(userRepository);
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
        UserService userService = new
                UserServiceServletImpl(userRepository);
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        Optional<User> foundUser =
                userService.findByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Поиск юзера по несуществующему ID")
    public void test_find_user_by_non_existing_id() {
        UserRepository mockStore = mock(UserRepository.class);
        UserService userService = new
                UserServiceServletImpl(mockStore);
        when(mockStore.findById(999)).thenReturn(null);

        Optional<User> foundUser = userService.findById(999);

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Поиск юзера по неправильному паролю")
    public void test_find_user_with_incorrect_password() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new
                UserServiceServletImpl(userRepository);
        when(userRepository.findByEmailAndPassword("test@example.com",
                "wrongpassword")).thenReturn(null);

        Optional<User> foundUser =
                userService.findByEmailAndPassword("test@example.com",
                        "wrongpassword");

        assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Поиск юзера по корректному ID")
    public void test_find_user_by_valid_id() {
        UserRepository mockRepository = mock(UserRepository.class);
        UserServiceServletImpl service = new
                UserServiceServletImpl(mockRepository);
        int userId = 1;
        User expectedUser = new User(userId, "valid@example.com",
                "password123", "1234", "John Doe", Role.CLIENT, false);
        when(mockRepository.findById(userId)).thenReturn(expectedUser);

        Optional<User> result = service.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

    @Test
    @DisplayName("Поиск юзера пол корректным емайл и паролю")
    public void test_find_user_by_email_and_password() {
        UserRepository mockRepository = mock(UserRepository.class);
        UserServiceServletImpl service = new
                UserServiceServletImpl(mockRepository);
        String email = "valid@example.com";
        String password = "password123";
        User expectedUser = new User(1, email, password, "1234", "John Doe", Role.CLIENT, false);
        when(mockRepository.findByEmailAndPassword(email,
                password)).thenReturn(expectedUser);

        Optional<User> result = service.findByEmailAndPassword(email, password);

        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());
    }

    @Test
    @DisplayName("Создание юзера с некорректным емайл")
    public void test_create_user_with_invalid_email() {
        UserRepository mockRepository = mock(UserRepository.class);
        UserServiceServletImpl service = new
                UserServiceServletImpl(mockRepository);
        String email = "invalid-email";
        String password = "password123";
        String name = "John Doe";

        Optional<User> result = service.createByReg(email, password, name);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Поиск юзера по не существующему емайл")
    public void test_find_user_by_non_existent_email() {
        UserRepository mockRepository = mock(UserRepository.class);
        UserServiceServletImpl service = new
                UserServiceServletImpl(mockRepository);
        String email = "nonexistent@example.com";
        when(mockRepository.findByEmail(email)).thenReturn(null);

        Optional<User> result = service.findByEmail(email);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Поиск юзера по несуществующему ID")
    public void test_find_user_by_non_existent_id() {
        UserRepository mockRepository = mock(UserRepository.class);
        UserServiceServletImpl service = new
                UserServiceServletImpl(mockRepository);
        int userId = 999;
        when(mockRepository.findById(userId)).thenReturn(null);

        Optional<User> result = service.findById(userId);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("УПоиск юзера по некорректным данным")
    public void test_find_user_by_incorrect_email_and_password() {
        UserRepository mockRepository = mock(UserRepository.class);
        UserServiceServletImpl service = new
                UserServiceServletImpl(mockRepository);
        String email = "wrong@example.com";
        String password = "wrongpassword";
        when(mockRepository.findByEmailAndPassword(email,
                password)).thenReturn(null);

        Optional<User> result = service.findByEmailAndPassword(email, password);

        assertFalse(result.isPresent());
    }

    @Test
    void createByReg() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByEmailAndPassword() {
    }

    @Test
    void findAll() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }

    @Test
    void updatePass() {
    }

    @Test
    void findByParameters() {
    }
}