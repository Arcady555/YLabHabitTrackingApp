package ru.parfenov.homework_1.server.store;

import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;

import java.util.List;

public interface HabitStore {
    Habit create(Habit habit);

    Habit delete(long habitId);

    List<Habit> findByUser(User user);

    Habit findById(long id);

    Habit update(Habit habit);

    Habit delete(Habit habit);
}