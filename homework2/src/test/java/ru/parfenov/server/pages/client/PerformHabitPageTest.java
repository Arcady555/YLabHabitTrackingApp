package ru.parfenov.server.pages.client;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.server.enums.user.Role;
import ru.parfenov.server.model.Habit;
import ru.parfenov.server.model.User;
import ru.parfenov.server.service.HabitService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class PerformHabitPageTest {

    @Test
    @DisplayName("Успешное выполнение привычки с валидным ID")
    public void test_valid_habit_id_performed_successfully() throws
            IOException, InterruptedException {
        User user = new User(1, "user@example.com", "password", "1", "User", null, false);
        HabitService habitService = mock(HabitService.class);
        Habit habit = new Habit(1, user, true, true, 0, "Habit",
                "Description", LocalDate.now(), LocalDate.now(), LocalDate.now(),
                LocalDate.now(), LocalDate.now(), Period.ofDays(1), 0);
        when(habitService.findById(1L)).thenReturn(Optional.of(habit));
        when(habitService.perform(habit)).thenReturn(true);
        PerformHabitPage performHabitPage = new PerformHabitPage(user,
                habitService);
        performHabitPage.reader = new BufferedReader(new StringReader("1\n"));
        performHabitPage.run();
        verify(habitService).perform(habit);
    }

    @Test
    @DisplayName("Не успешное выполнение привычки с валидным ID")
    public void test_valid_habit_id_not_performed() throws
            IOException, InterruptedException {
        User user = new User(1, "user@example.com", "password", "1", "User", null, false);
        HabitService habitService = mock(HabitService.class);
        Habit habit = new Habit(1, user, true, true, 0, "Habit",
                "Description", LocalDate.now(), LocalDate.now(), LocalDate.now(),
                LocalDate.now(), LocalDate.now(), Period.ofDays(1), 0);
        when(habitService.findById(1L)).thenReturn(Optional.of(habit));
        when(habitService.perform(habit)).thenReturn(false);
        PerformHabitPage performHabitPage = new PerformHabitPage(user,
                habitService);
        performHabitPage.reader = new BufferedReader(new StringReader("1\n"));
        performHabitPage.run();
        verify(habitService).perform(habit);
    }

    @Test
    @DisplayName("Успешное выполнение привычки, принадлежащей юзеру")
    public void test_valid_habit_id_belongs_to_user() throws
            IOException, InterruptedException {
        User user = new User(1, "user@example.com", "password", "1", "User", null, false);
        HabitService habitService = mock(HabitService.class);
        Habit habit = new Habit(1, user, true, true, 0, "Habit",
                "Description", LocalDate.now(), LocalDate.now(), LocalDate.now(),
                LocalDate.now(), LocalDate.now(), Period.ofDays(1), 0);
        when(habitService.findById(1L)).thenReturn(Optional.of(habit));
        PerformHabitPage performHabitPage = new PerformHabitPage(user,
                habitService);
        performHabitPage.reader = new BufferedReader(new StringReader("1\n"));
        performHabitPage.run();
        verify(habitService).perform(habit);
    }

    @Test
    @DisplayName("Привычка не найдена")
    public void test_valid_habit_id_does_not_exist() throws
            IOException, InterruptedException {
        User user = new User(1, "user@example.com", "password", "1", "User", null, false);
        HabitService habitService = mock(HabitService.class);
        Habit habit = new Habit(1, user, true, true, 0, "Habit",
                "Description", LocalDate.now(), LocalDate.now(), LocalDate.now(),
                LocalDate.now(), LocalDate.now(), Period.ofDays(1), 0);
        when(habitService.findById(1L)).thenReturn(Optional.empty());
        PerformHabitPage performHabitPage = new PerformHabitPage(user,
                habitService);
        performHabitPage.reader = new BufferedReader(new StringReader("1\n"));
        performHabitPage.run();
        verify(habitService).findById(1L);
    }

    @Test
    @DisplayName("Привычка не принадлежит юзеру")
    public void test_habit_id_not_associated_with_any_habit() throws
            IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password",
                "reset", "Test User", Role.CLIENT, false);
        HabitService habitService = mock(HabitService.class);
        when(habitService.findById(999L)).thenReturn(Optional.empty());
        PerformHabitPage page = new PerformHabitPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("999");
        page.reader = reader;
        page.run();
        verify(habitService).findById(999L);
    }
}