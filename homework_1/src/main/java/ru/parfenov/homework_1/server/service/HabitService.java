package ru.parfenov.homework_1.server.service;

import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

public interface HabitService {
    Habit create(
            User user,
            boolean usefulness,
            String name,
            String description,
            LocalDate dateOfCreate,
            LocalDate firstPerform,
            Period frequency
    );

    Optional<Habit> findById(long id);

    List<Habit> findByUser(User user);

    boolean perform(Habit habit);

    Habit update(Habit habit);

    String remind(long habitId);

    List<Habit> todayPerforms(User user);

    String statistic(long habitId, LocalDate dateFrom, LocalDate dateTo);
}