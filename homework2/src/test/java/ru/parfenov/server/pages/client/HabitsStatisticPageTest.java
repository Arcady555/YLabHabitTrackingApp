package ru.parfenov.server.pages.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.server.model.Habit;
import ru.parfenov.server.model.User;
import ru.parfenov.server.service.HabitService;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class HabitsStatisticPageTest {

    @Test
    @DisplayName("Ввод дат")
    public void test_user_prompted_to_enter_dates() throws
            IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        HabitService habitService = mock(HabitService.class);
        HabitsStatisticPage page = new HabitsStatisticPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenReturn("2023-01-01", "2023-12-31");

        page.run();

        verify(reader, times(2)).readLine();
    }

    @Test
    @DisplayName("Привычек юзера не найдено")
    public void test_no_habits_associated_with_user() throws
            IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        HabitService habitService = mock(HabitService.class);
        HabitsStatisticPage page = new HabitsStatisticPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenReturn("2023-01-01", "2023-12-31");
        when(habitService.findByUser(user)).thenReturn(List.of());

        page.run();

        verify(habitService).findByUser(user);
    }

    @Test
    @DisplayName("Ввод корректных данных")
    public void test_correct_date_format() throws IOException,
            InterruptedException {
        User user = new User();
        HabitService habitService = mock(HabitService.class);
        HabitsStatisticPage page = new HabitsStatisticPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenReturn("2023-01-01", "2023-01-31");

        page.run();

        verify(reader, times(2)).readLine();
    }

    @Test
    @DisplayName("Возврат списка привычек пользователя и отображение статистики")
    public void test_habits_list_and_statistics_displayed() throws
            IOException, InterruptedException {
        User user = new User();
        HabitService habitService = mock(HabitService.class);
        HabitsStatisticPage page = new HabitsStatisticPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenReturn("2023-01-01", "2023-01-31");
        List<Habit> habits = Arrays.asList(new Habit(1, user, true,
                true, 0, "Exercise", "Daily exercise", LocalDate.now(),
                LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now(),
                Period.ofDays(1), 0));
        when(habitService.findByUser(user)).thenReturn(habits);

        page.run();

        verify(habitService).findByUser(user);
    }

    @Test
    @DisplayName("Привычек в диапазоне не найдено")
    public void test_no_habits_in_date_range() throws IOException,
            InterruptedException {
        User user = new User();
        HabitService habitService = mock(HabitService.class);
        HabitsStatisticPage page = new HabitsStatisticPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenReturn("2023-01-01", "2023-01-31");
        when(habitService.findByUser(user)).thenReturn(Collections.emptyList());

        page.run();

        verify(habitService).findByUser(user);
    }

    @Test
    @DisplayName("Ввод одинаковых дат для старта и финиша")
    public void test_same_start_end_date_processing() throws
            IOException, InterruptedException {
        User user = new User();
        HabitService habitService = mock(HabitService.class);
        HabitsStatisticPage page = new HabitsStatisticPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        page.reader = reader;

        when(reader.readLine()).thenReturn("2023-01-01", "2023-01-01");

        page.run();

        verify(reader, times(2)).readLine();
    }
}