package ru.parfenov.homework_2.server.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_2.server.model.Habit;
import ru.parfenov.homework_2.server.model.User;
import ru.parfenov.homework_2.server.repository.HabitRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

public class HabitServiceConsoleImplTest {

    @Test
    @DisplayName("Успешное создание привычки")
    public void test_create_habit_success() {
        HabitRepository habitRepository = mock(HabitRepository.class);
        HabitServiceConsoleImpl service = new
                HabitServiceConsoleImpl(habitRepository);
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        LocalDate dateOfCreate = LocalDate.now();
        LocalDate firstPerform = dateOfCreate.plusDays(1);
        Period frequency = Period.ofDays(1);
        Habit habit = new Habit(0L, user, true, true, 1, "Test Habit",
                "Description", dateOfCreate, firstPerform, null, firstPerform, null,
                frequency, 0);

        when(habitRepository.create(any(Habit.class))).thenReturn(habit);

        Habit createdHabit = service.create(user, true, "Test Habit",
                "Description", dateOfCreate, firstPerform, frequency).get();

        assertNotNull(createdHabit);
        assertEquals("Test Habit", createdHabit.getName());
        verify(habitRepository).create(any(Habit.class));
    }

    @Test
    @DisplayName("Успешное удаление привычки")
    public void test_delete_habit_success() {
        HabitRepository habitRepository = mock(HabitRepository.class);
        HabitServiceConsoleImpl service = new
                HabitServiceConsoleImpl(habitRepository);
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
        HabitServiceConsoleImpl service = new
                HabitServiceConsoleImpl(habitRepository);
        long habitId = 1L;
        Habit habit = new Habit();
        habit.setId(habitId);

        when(habitRepository.findById(habitId)).thenReturn(habit);

        Optional<Habit> foundHabit = service.findById(habitId);

        assertTrue(foundHabit.isPresent());
        assertEquals(habitId, foundHabit.get().getId());
        verify(habitRepository).findById(habitId);
    }
}