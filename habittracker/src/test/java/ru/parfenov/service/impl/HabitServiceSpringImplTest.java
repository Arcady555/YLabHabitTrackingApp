package ru.parfenov.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.dto.habit.HabitCreateDTO;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.repository.HabitRepository;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.service.HabitService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class HabitServiceSpringImplTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    HabitService habitService;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository, @Autowired HabitRepository habitRepository) throws Exception {
        User user = new User(0, "user2@mail.ru", "password2", "1111", "user2", Role.CLIENT, false);
        userRepository.save(user);
        habitRepository.save(new Habit(
                0,
                user,
                true,
                true,
                1,
                "run",
                "run everyday",
                LocalDate.now(),
                LocalDate.now(),
                null,
                LocalDate.now(),
                null,
                Period.of(0, 0, 10),
                0
                )
        );
    }

    @Test
    @DisplayName("Найти привычку по ID")
    public void test_find_habit_by_id_success() {
        Optional<Habit> foundHabit = habitService.findById(1);

        Assertions.assertTrue(foundHabit.isPresent());
        Assertions.assertEquals(1, foundHabit.get().getId());
    }

    @Test
    @WithMockUser(roles = {"CLIENT"}, username = "user2@mail.ru")
    @DisplayName("Создать привычку")
    public void test_create() {
        Optional<HabitGeneralDTO> habit = habitService.create(new HabitCreateDTO(
                "true", "бегать", "Бегать каждый день", LocalDate.now().toString(), 1)
        );

        Assertions.assertTrue(habit.isPresent());
        Assertions.assertEquals(2, habit.get().getId());
    }

    @Test
    @WithMockUser(roles = {"CLIENT"}, username = "user2@mail.ru")
    @DisplayName("Удалить привычку")
    public void test_delete() {
        boolean result = habitService.delete(2);

        Assertions.assertTrue(result);
        Assertions.assertTrue(habitService.findById(2).isEmpty());
    }

    @Test
    @WithMockUser(roles = {"CLIENT"}, username = "user2@mail.ru")
    @DisplayName("Поиск всех привычек юзера")
    public void test_find_by_user() {
        List<HabitGeneralDTO> foundHabits = habitService.findByUser();

        Assertions.assertFalse(foundHabits.isEmpty());
        Assertions.assertEquals("run", foundHabits.get(0).getName());
    }
}