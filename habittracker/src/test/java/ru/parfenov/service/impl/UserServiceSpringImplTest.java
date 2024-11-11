package ru.parfenov.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.dto.user.UserGeneralDTO;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserServiceSpringImplTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    UserService userService;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository) throws Exception {
        userRepository.save(new User(0, "user2@mail.ru", "password2", "1111", "user2", Role.CLIENT, false));
    }

    @Test
    @DisplayName("Успешное создание юзера")
    public void test_create_user_success() {
        UserGeneralDTO createdUser = userService.createByReg("test@example.com", "password", "Test User").get();

        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals("test@example.com", createdUser.getEmail());
        Assertions.assertEquals("Test User", createdUser.getName());
    }

    @Test
    @DisplayName("Найти существующего юзера по емайл")
    public void test_find_user_by_email_success() {
        Optional<User> foundUser = userService.findByEmail("user2@mail.ru");

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals("user2@mail.ru", foundUser.get().getEmail());
    }

    @Test
    @DisplayName("Найти юзера по ID")
    public void test_find_user_by_id_success() {
        Optional<UserGeneralDTO> foundUser = userService.findById(1);

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertEquals(1, foundUser.get().getId());
    }

    @Test
    @DisplayName("Поиск юзера по несуществующему емайл")
    public void test_find_user_by_non_existing_email() {
        Optional<User> foundUser = userService.findByEmail("nonexistent@example.com");

        Assertions.assertFalse(foundUser.isPresent());
    }

    @Test
    @DisplayName("Поиск юзера по несуществующему ID")
    public void test_find_user_by_non_existing_id() {
        Optional<UserGeneralDTO> foundUser = userService.findById(999);

        Assertions.assertFalse(foundUser.isPresent());
    }


    @Test
    @DisplayName("Создание юзера с некорректным емайл")
    public void test_create_user_with_invalid_email() {
        String email = "invalid-email";
        String password = "password123";
        String name = "John Doe";

        Optional<UserGeneralDTO> result = userService.createByReg(email, password, name);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Поиск юзера по не существующему емайл")
    public void test_find_user_by_non_existent_email() {
        String email = "nonexistent@example.com";

        Optional<User> result = userService.findByEmail(email);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Поиск юзера по несуществующему ID")
    public void test_find_user_by_non_existent_id() {
        int userId = 999;

        Optional<UserGeneralDTO> result = userService.findById(userId);

        Assertions.assertFalse(result.isPresent());
    }
}