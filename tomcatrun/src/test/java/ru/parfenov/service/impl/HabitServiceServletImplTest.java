package ru.parfenov.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.dto.habit.HabitCreateDTO;
import ru.parfenov.dto.habit.HabitDTOMapper;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.dto.habit.HabitUpdateDTO;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.repository.HabitRepository;
import ru.parfenov.service.HabitService;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HabitServiceServletImplTest {

    @Test
    @DisplayName("Успешное создание привычки")
    public void test_create_habit_success() {
        HabitRepository habitRepository = mock(HabitRepository.class);
        HabitService service = new
                HabitServiceServletImpl(habitRepository);
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        LocalDate dateOfCreate = LocalDate.now();
        LocalDate firstPerform = dateOfCreate.plusDays(1);
        Period frequency = Period.ofDays(1);
        Habit habit = new Habit(0L, user, true, true, 1, "Test Habit",
                "Description", dateOfCreate, firstPerform, null, firstPerform, null,
                frequency, 0);

        when(habitRepository.create(any(Habit.class))).thenReturn(habit);
        HabitGeneralDTO createdHabit = service.create(user, HabitDTOMapper.toHabitCreateDTO(habit)).get();

        assertNotNull(createdHabit);
        assertEquals("Test Habit", createdHabit.getName());
        verify(habitRepository).create(any(Habit.class));
    }

    @Test
    @DisplayName("Успешное удаление привычки")
    public void test_delete_habit_success() {
        HabitRepository habitRepository = mock(HabitRepository.class);
        HabitService service = new
                HabitServiceServletImpl(habitRepository);
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
        HabitService service = new
                HabitServiceServletImpl(habitRepository);
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
        HabitServiceServletImpl service = new
                HabitServiceServletImpl(repository);
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
        HabitServiceServletImpl service = new
                HabitServiceServletImpl(repository);
        when(repository.findById(1L)).thenReturn(habit);

        Optional<Habit> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Habit", result.get().getName());
    }

    @Test
    @DisplayName("Не успешное обновление привычки")
    public void test_update_habit_mismatched_user() {
        User user1 = new User(1, "user1@example.com", "password",
                null, "User One", Role.CLIENT, false);
        User user2 = new User(2, "user2@example.com", "password",
                null, "User Two", Role.CLIENT, false);
        HabitUpdateDTO habitDTO = new HabitUpdateDTO(1L, "true",
                "true", "Updated Name", "Updated Description", 7);
        Habit habit = new Habit(1L, user2, true, true, 1, "Original Name", "Original Description", LocalDate.now(),
                LocalDate.parse("2023-10-01"), null, LocalDate.parse("2023-10-01"),
                null, Period.ofDays(7), 0);
        HabitRepository repository = mock(HabitRepository.class);
        HabitServiceServletImpl service = new
                HabitServiceServletImpl(repository);
        when(repository.findById(1L)).thenReturn(habit);

        Optional<HabitGeneralDTO> result = service.updateByUser(user1,
                habitDTO);

        assertFalse(result.isPresent());
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