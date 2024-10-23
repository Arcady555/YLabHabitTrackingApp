package ru.parfenov.server.pages.client;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.server.enums.user.Role;
import ru.parfenov.server.model.User;
import ru.parfenov.server.service.HabitService;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class CreateHabitPageTest {

    @Test
    @DisplayName("Ввод 0 -> привычка полезная")
    public void test_usefulness_input_zero() throws IOException {
        HabitService habitService = mock(HabitService.class);
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        CreateHabitPage createHabitPage = new CreateHabitPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        createHabitPage.reader = reader;
        String firstPerformDate = LocalDate.now().plusDays(10L).toString();

        when(reader.readLine()).thenReturn("0", "Exercise", "Daily exercise", firstPerformDate, "30");

        createHabitPage.run();

        verify(habitService).create(eq(user), eq(true), anyString(),
                anyString(), any(), any(), any());
    }

    @Test
    @DisplayName("Ввод валидной даты для первого выполнения привычки")
    public void test_valid_future_date_for_first_performance() throws
            IOException {
        HabitService habitService = mock(HabitService.class);
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        CreateHabitPage createHabitPage = new CreateHabitPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        createHabitPage.reader = reader;
        String firstPerformDate = LocalDate.now().plusDays(10L).toString();

        when(reader.readLine()).thenReturn("0", "Exercise", "Daily exercise", firstPerformDate, "30");

        createHabitPage.run();

        verify(habitService).create(any(), anyBoolean(), anyString(),
                anyString(), any(), eq(LocalDate.parse(firstPerformDate)), any());
    }

    @Test
    @DisplayName("Создание привычки с предоставленными входными данными.")
    public void test_calls_habit_service_create() throws IOException {
        User user = new User();
        HabitService habitService = mock(HabitService.class);
        CreateHabitPage createHabitPage = new CreateHabitPage(user,
                habitService);
        BufferedReader reader = mock(BufferedReader.class);
        createHabitPage.reader = reader;
        LocalDate date = LocalDate.now().plusDays(30L);
        when(reader.readLine()).thenReturn("0", "Exercise", "Daily exercise", date.toString(), "30");

        createHabitPage.run();

        verify(habitService).create(any(), anyBoolean(), anyString(),
                anyString(), any(), any(), any());
    }
}