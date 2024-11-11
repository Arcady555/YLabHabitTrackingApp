package ru.parfenov.repository;

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
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    UserRepository userRepository;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository) throws Exception {
        userRepository.save(new User(0, "user2@mail.ru", "password2", "1111", "user2", Role.CLIENT, false));
    }

    @Test
    @DisplayName("Проверка findAll()")
    void whenCreateAndGetAllThanOk() {
        User foundedUser = userRepository.findAll().get(1);
        Assertions.assertEquals(foundedUser.getId(), 2);
        Assertions.assertEquals(foundedUser.getEmail(), "user2@mail.ru");
        Assertions.assertEquals(foundedUser.getPassword(), "password2");
        Assertions.assertEquals(foundedUser.getResetPassword(), "1111");
        Assertions.assertEquals(foundedUser.getName(), "user2");
        Assertions.assertEquals(foundedUser.getRole(), Role.CLIENT);
        Assertions.assertFalse(foundedUser.isBlocked());
    }

    @Test
    @DisplayName("Проверка findById()")
    void whenCreateAndFindByIdThanOk() {
        User foundedUser = userRepository.findById(2).get();
        Assertions.assertEquals(foundedUser.getId(), 2);
        Assertions.assertEquals(foundedUser.getEmail(), "user2@mail.ru");
        Assertions.assertEquals(foundedUser.getPassword(), "password2");
        Assertions.assertEquals(foundedUser.getResetPassword(), "1111");
        Assertions.assertEquals(foundedUser.getName(), "user2");
        Assertions.assertEquals(foundedUser.getRole(), Role.CLIENT);
        Assertions.assertFalse(foundedUser.isBlocked());
    }

    @Test
    @DisplayName("Проверка findByEmail()")
    void whenCreateAndFindByEmailThanOk() {
        User foundedUser = userRepository.findByEmail("user2@mail.ru").get();
        Assertions.assertEquals(foundedUser.getId(), 2);
        Assertions.assertEquals(foundedUser.getPassword(), "password2");
        Assertions.assertEquals(foundedUser.getResetPassword(), "1111");
        Assertions.assertEquals(foundedUser.getName(), "user2");
        Assertions.assertEquals(foundedUser.getRole(), Role.CLIENT);
        Assertions.assertFalse(foundedUser.isBlocked());
    }

    @Test
    @DisplayName("Проверка findByEmailAndPassword()")
    void whenCreateAndFindByEmailAndPasswordThanOk() {
        User foundedUser = userRepository.findByEmailAndPassword("user2@mail.ru", "password2").get();
        Assertions.assertEquals(foundedUser.getId(), 2);
        Assertions.assertEquals(foundedUser.getPassword(), "password2");
        Assertions.assertEquals(foundedUser.getResetPassword(), "1111");
        Assertions.assertEquals(foundedUser.getName(), "user2");
        Assertions.assertEquals(foundedUser.getRole(), Role.CLIENT);
        Assertions.assertFalse(foundedUser.isBlocked());
    }
}