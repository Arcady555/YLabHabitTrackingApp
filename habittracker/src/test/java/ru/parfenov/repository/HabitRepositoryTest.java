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
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class HabitRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15");
    static User user;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HabitRepository habitRepository;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository, @Autowired HabitRepository habitRepository) throws Exception {
        user = new User(0, "user2@mail.ru", "password2", "1111", "user2", Role.CLIENT, false);
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
    @DisplayName("Проверка findByUser()")
    void whenCreateAndFindByUserThanOk() {
        Habit foundedHabit = habitRepository.findByUser(user).get(0);
        Assertions.assertEquals(foundedHabit.getId(), 1);
        Assertions.assertEquals(foundedHabit.getUser().getEmail(), user.getEmail());
        Assertions.assertTrue(foundedHabit.isUseful());
        Assertions.assertTrue(foundedHabit.isActive());
        Assertions.assertEquals(foundedHabit.getStreaksAmount(), 1);
        Assertions.assertEquals(foundedHabit.getName(), "run");
        Assertions.assertEquals(foundedHabit.getDescription(), "run everyday");
        Assertions.assertEquals(foundedHabit.getDateOfCreate(), LocalDate.now());
        Assertions.assertEquals(foundedHabit.getPlannedFirstPerform(), LocalDate.now());
        Assertions.assertNull(foundedHabit.getPlannedPrevPerform());
        Assertions.assertEquals(foundedHabit.getPlannedNextPerform(), LocalDate.now());
        Assertions.assertNull(foundedHabit.getLastRealPerform());
        Assertions.assertEquals(foundedHabit.getFrequency(), Period.of(0, 0, 10));
        Assertions.assertEquals(foundedHabit.getPerformsAmount(), 0);
    }

    @Test
    @DisplayName("Проверка findById()")
    void whenCreateAndFindByIdThanOk() {
        Habit foundedHabit = habitRepository.findById(1).get();
        Assertions.assertEquals(foundedHabit.getId(), 1);
        Assertions.assertEquals(foundedHabit.getUser().getId(), user.getId());
        Assertions.assertTrue(foundedHabit.isUseful());
        Assertions.assertTrue(foundedHabit.isActive());
        Assertions.assertEquals(foundedHabit.getStreaksAmount(), 1);
        Assertions.assertEquals(foundedHabit.getName(), "run");
        Assertions.assertEquals(foundedHabit.getDescription(), "run everyday");
        Assertions.assertEquals(foundedHabit.getDateOfCreate(), LocalDate.now());
        Assertions.assertEquals(foundedHabit.getPlannedFirstPerform(), LocalDate.now());
        Assertions.assertNull(foundedHabit.getPlannedPrevPerform());
        Assertions.assertEquals(foundedHabit.getPlannedNextPerform(), LocalDate.now());
        Assertions.assertNull(foundedHabit.getLastRealPerform());
        Assertions.assertEquals(foundedHabit.getFrequency(), Period.of(0, 0, 10));
        Assertions.assertEquals(foundedHabit.getPerformsAmount(), 0);
    }

    @Test
    @DisplayName("Проверка findByUserForToday()")
    void whenCreateAndFindByUserForToday() {
        List<Habit> foundedHabits = habitRepository.findByUserForToday(user);
        Assertions.assertEquals(foundedHabits.get(0).getId(), 1);
        Assertions.assertEquals(foundedHabits.get(0).getUser().getId(), user.getId());
        Assertions.assertTrue(foundedHabits.get(0).isUseful());
        Assertions.assertTrue(foundedHabits.get(0).isActive());
        Assertions.assertEquals(foundedHabits.get(0).getStreaksAmount(), 1);
        Assertions.assertEquals(foundedHabits.get(0).getName(), "run");
        Assertions.assertEquals(foundedHabits.get(0).getDescription(), "run everyday");
        Assertions.assertEquals(foundedHabits.get(0).getDateOfCreate(), LocalDate.now());
        Assertions.assertEquals(foundedHabits.get(0).getPlannedFirstPerform(), LocalDate.now().plusDays(0L));
        Assertions.assertNull(foundedHabits.get(0).getPlannedPrevPerform());
        Assertions.assertEquals(foundedHabits.get(0).getPlannedNextPerform(), LocalDate.now().plusDays(0L));
        Assertions.assertNull(foundedHabits.get(0).getLastRealPerform());
        Assertions.assertEquals(foundedHabits.get(0).getFrequency(), Period.of(0, 0, 10));
        Assertions.assertEquals(foundedHabits.get(0).getPerformsAmount(), 0);
    }

}