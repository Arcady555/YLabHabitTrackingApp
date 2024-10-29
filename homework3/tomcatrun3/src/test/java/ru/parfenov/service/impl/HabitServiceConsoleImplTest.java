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

public class HabitServiceConsoleImplTest {
/*
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

    // Creating a habit successfully returns a HabitGeneralDTO
    @Test
    public void test_create_habitsuccess() {
        User user = new User(1, "test@example.com", "password", null,
                "Test User", Role.CLIENT, false);
        HabitCreateDTO habitDTO = new HabitCreateDTO("true", "Test Habit", "Description", "2023-10-01", 7);
                HabitRepository repository = mock(HabitRepository.class);
        HabitServiceServletImpl service = new
                HabitServiceServletImpl(repository);
        Habit habit = new Habit(1L, user, true, true, 1, "Test Habit",
                "Description", LocalDate.now(), LocalDate.parse("2023-10-01"), null,
                LocalDate.parse("2023-10-01"), null, Period.ofDays(7), 0);
        when(repository.create(any(Habit.class))).thenReturn(habit);

        Optional<HabitGeneralDTO> result = service.create(user, habitDTO);

        assertTrue(result.isPresent());
        assertEquals("Test Habit", result.get().getName());
    }

    // Deleting a habit by ID returns true when the habit is removed
    @Test
    public void testdelete_habit_success() {
        HabitRepository repository = mock(HabitRepository.class);
        HabitServiceServletImpl service = new
                HabitServiceServletImpl(repository);
        when(repository.findById(1L)).thenReturn(null);

        boolean result = service.delete(1L);

        assertTrue(result);
    }

    // Deleting all habits for a user returns true when all are removed
    @Test
    public void test_delete_all_habits_for_user_success() {
        User user = new User(1, "test@example.com", "password", null,
                "Test User", Role.CLIENT, false);
        HabitRepository repository = mock(HabitRepository.class);
        HabitServiceServletImpl service = new
                HabitServiceServletImpl(repository);
        when(repository.findByUser(user)).thenReturn(new ArrayList<>());

        boolean result = service.deleteWithUser(user);

        assertTrue(result);
    }

    // Finding a habit by ID returns an Optional with the habit if it exists
    @Test
    public void test_find_habit_by_idsuccess() {
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

    // Creating a habit with invalid data returns an empty Optional
    @Test
    public void test_create_habit_invalid_data() {
        User user = new User(1, "test@example.com", "password", null,
                "Test User", Role.CLIENT, false);
        HabitCreateDTO habitDTO = new HabitCreateDTO(null, null, null, null, 0);
        HabitRepository repository = mock(HabitRepository.class);
        HabitServiceServletImpl service = new
                HabitServiceServletImpl(repository);

        Optional<HabitGeneralDTO> result = service.create(user, habitDTO);

        assertFalse(result.isPresent());
    }

    // Deleting a non-existent habit returns false
    @Test
    public void test_delete_non_existent_habit() {
        HabitRepository repository = mock(HabitRepository.class);
        HabitServiceServletImpl service = new
                HabitServiceServletImpl(repository);
        when(repository.findById(999L)).thenReturn(null);

        boolean result = service.delete(999L);

        assertFalse(result);
    }

    // Performing a habit with an invalid ID string returns an empty Optional
    @Test
    public void test_perform_habit_invalid_id_string() {
        HabitRepository repository = mock(HabitRepository.class);
        HabitServiceServletImpl service = new
                HabitServiceServletImpl(repository);

        Optional<Habit> result = service.perform("invalid");

        assertFalse(result.isPresent());
    }

    // Updating a habit with mismatched user returns an empty Optional
    @Test
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
    } */
}