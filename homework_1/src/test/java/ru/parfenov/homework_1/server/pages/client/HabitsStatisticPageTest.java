package ru.parfenov.homework_1.server.pages.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.HabitService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

public class HabitsStatisticPageTest {
    User user = new User(1, "test@example.com", "password", "Test User", null, false);
    HabitService habitService = mock(HabitService.class);
    HabitsStatisticPage page = new HabitsStatisticPage(user, habitService);
    BufferedReader reader = mock(BufferedReader.class);

    @Test
    @DisplayName("Ввод дат")
    public void test_user_prompted_to_enter_dates() throws
            IOException, InterruptedException {
        page.reader = reader;

        when(reader.readLine()).thenReturn("2023-01-01", "2023-12-31");

        page.run();

        verify(reader, times(2)).readLine();
    }

    @Test
    @DisplayName("Привычек не найдено")
    public void test_no_habits_associated_with_user() throws
            IOException, InterruptedException {

        page.reader = reader;

        when(reader.readLine()).thenReturn("2023-01-01", "2023-12-31");
        when(habitService.findByUser(user)).thenReturn(List.of());

        page.run();

        verify(habitService).findByUser(user);
    }
}