package ru.parfenov.homework_1.server.pages.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.HabitService;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class YourHabitListByParametersPageTest {

    @Test
    @DisplayName("Успешный вывод всех привычек")
    public void test_valid_parameters_return_habits() throws
            IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        HabitService habitService = mock(HabitService.class);
        YourHabitListByParametersPage page = new YourHabitListByParametersPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenReturn("0", "0", "exercise",
                "daily routine", "2023-01-01", "weekly");
        List<Habit> habits = List.of(new Habit(1, user, true, true, 5,
                "exercise", "daily routine", LocalDate.now(), LocalDate.now(),
                LocalDate.now(), LocalDate.now(), LocalDate.now(), Period.ofDays(7),
                10));
        when(habitService.findByParameters(any(), anyString(),
                anyString(), anyString(), anyString(), anyString(),
                anyString())).thenReturn(habits);

        page.run();

        verify(habitService).findByParameters(user, "true", "true",
                "exercise", "daily routine", "2023-01-01", "weekly");
    }

    @Test
    @DisplayName("Ввод 0 или 1 для полезности и активности")
    public void test_usefulness_and_active_input() throws IOException,
            InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        HabitService habitService = mock(HabitService.class);
        YourHabitListByParametersPage page = new YourHabitListByParametersPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenReturn("0", "1", "", "", "", "");

        page.run();

        verify(habitService).findByParameters(user, "true", "false",
                "", "", "", "");
    }

    @Test
    @DisplayName("Возврат привычек по параметрам")
    public void test_habit_service_returns_matching_habits() throws
            IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        HabitService habitService = mock(HabitService.class);
        YourHabitListByParametersPage page = new YourHabitListByParametersPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenReturn("0", "0", "exercise", "", "", "");
        List<Habit> habits = List.of(new Habit(1, user, true, true, 5,
                "exercise", "", LocalDate.now(), LocalDate.now(), LocalDate.now(),
                LocalDate.now(), LocalDate.now(), Period.ofDays(7), 10));
        when(habitService.findByParameters(any(), anyString(),
                anyString(), anyString(), anyString(), anyString(),
                anyString())).thenReturn(habits);

        page.run();

        assertEquals(1, habits.size());
    }

    @Test
    @DisplayName("Вывод с remind")
    public void test_print_habit_with_reminder() throws IOException,
            InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        HabitService habitService = mock(HabitService.class);
        YourHabitListByParametersPage page = new YourHabitListByParametersPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenReturn("0", "0", "", "", "", "");
        Habit habit = new Habit(1, user, true, true, 5, "exercise",
                "", LocalDate.now(), LocalDate.now(), LocalDate.now(),
                LocalDate.now(), LocalDate.now(), Period.ofDays(7), 10);
        when(habitService.findByParameters(any(), anyString(),
                anyString(), anyString(), anyString(), anyString(),
                anyString())).thenReturn(List.of(habit));
        when(habitService.remind(habit.getId())).thenReturn("Reminder message");

        page.run();

        verify(habitService).remind(habit.getId());
    }

    @Test
    @DisplayName("Не валидные параметры")
    public void test_invalid_usefulness_and_active_input() throws
            IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        HabitService habitService = mock(HabitService.class);
        YourHabitListByParametersPage page = new YourHabitListByParametersPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenReturn("invalid", "invalid", "",
                "", "", "");

        page.run();

        verify(habitService).findByParameters(user, "", "", "", "", "", "");
    }

    @Test
    @DisplayName("ввод пустых строк")
    public void test_empty_string_inputs() throws IOException,
            InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        HabitService habitService = mock(HabitService.class);
        YourHabitListByParametersPage page = new YourHabitListByParametersPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenReturn("0", "0", "", "", "", "");

        page.run();

        verify(habitService).findByParameters(user, "true", "true",
                "", "", "", "");
    }

    @Test
    @DisplayName("Выброс IO Exception")
    public void test_io_exception_during_input_reading() throws IOException {
        User user = new User(1, "test@example.com", "password", "1","Test User", Role.CLIENT, false);
        HabitService habitService = mock(HabitService.class);
        YourHabitListByParametersPage page = new YourHabitListByParametersPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenThrow(new IOException("IO Exception"));

        assertThrows(IOException.class, page::run);
    }
}