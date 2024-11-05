package ru.parfenov.service.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.parfenov.dto.habit.HabitCreateDTO;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.dto.habit.HabitUpdateDTO;
import ru.parfenov.dto.habit.mapper.HabitDTOMapper;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.repository.HabitRepository;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HabitServiceServletImplTest {
    @Autowired
    private HabitDTOMapper habitDTOMapper;

    @Test
    @DisplayName("Успешное удаление привычки")
    public void test_delete_habit_success() {
        HabitRepository habitRepository = mock(HabitRepository.class);
        UserService userService = mock(UserService.class);
        HabitService service = new
                HabitServiceServletImpl(habitRepository, userService, habitDTOMapper);
        long habitId = 1L;

        doNothing().when(habitRepository).delete(isA(Long.class));
        boolean result = service.delete(habitId);

        assertTrue(result);
        verify(habitRepository).delete(habitId);
    }

    @Test
    @DisplayName("Найти привычку по  ID")
    public void test_find_habit_by_id_success() {
        HabitRepository habitRepository = mock(HabitRepository.class);
        UserService userService = mock(UserService.class);
        HabitService service = new
                HabitServiceServletImpl(habitRepository, userService, habitDTOMapper);
        long habitId = 1L;
        Habit habit = new Habit();
        habit.setId(habitId);

        when(habitRepository.findById(habitId)).thenReturn(habit);

        Optional<Habit> foundHabit = service.findById(habitId);

        assertTrue(foundHabit.isPresent());
        assertEquals(habitId, foundHabit.get().getId());
        verify(habitRepository).findById(habitId);
    }

    @Test
    @DisplayName("Успешное создание привычек юзера")
    public void test_delete_all_habits_for_user_success() {
        User user = new User(1, "test@example.com", "password", null,
                "Test User", Role.CLIENT, false);
        HabitRepository repository = mock(HabitRepository.class);
        UserService userService = mock(UserService.class);
        HabitServiceServletImpl service = new
                HabitServiceServletImpl(repository, userService, habitDTOMapper);
        when(repository.findByUser(user.getId())).thenReturn(new ArrayList<>());

        boolean result = service.deleteWithUser(Integer.toString(user.getId()));

        assertTrue(result);
    }

    @Test
    @DisplayName("Поиск привычки")
    public void test_find_habit_by_success() {
        User user = new User(1, "test@example.com", "password", null,
                "Test User", Role.CLIENT, false);
        Habit habit = new Habit(1L, user, true, true, 1, "Test Habit",
                "Description", LocalDate.now(), LocalDate.parse("2023-10-01"), null,
                LocalDate.parse("2023-10-01"), null, Period.ofDays(7), 0);
        HabitRepository repository = mock(HabitRepository.class);
        UserService userService = mock(UserService.class);
        HabitServiceServletImpl service = new
                HabitServiceServletImpl(repository, userService, habitDTOMapper);
        when(repository.findById(1L)).thenReturn(habit);

        Optional<Habit> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Habit", result.get().getName());
    }

    @Test
    void create() {
    }

    @Test
    void delete() {
    }

    @Test
    void deleteWithUser() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByUser() {
    }

    @Test
    void perform() {
    }

    @Test
    void updateByUser() {
    }

    @Test
    void todayPerforms() {
    }

    @Test
    void statisticForUser() {
    }

    @Test
    void findByParameters() {
    }
}