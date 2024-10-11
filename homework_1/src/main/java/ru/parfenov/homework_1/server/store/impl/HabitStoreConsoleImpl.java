package ru.parfenov.homework_1.server.store.impl;

import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.store.HabitStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabitStoreConsoleImpl implements HabitStore {
    private static long habitId = 0L;
    private final Map<Long, Habit> habitMap = new HashMap<>();

    @Override
    public Habit create(Habit habit) {
        habitId++;
        habit.setId(habitId);
        habitMap.put(habitId, habit);
        return habitMap.get(habitId);
    }

    @Override
    public List<Habit> findByUser(User user) {
        List<Habit> result = new ArrayList<>();
        for (Habit habit : habitMap.values()) {
            if (user.equals(habit.getUser())) result.add(habit);
        }
        return result;
    }

    @Override
    public Habit findById(long id) {
        return habitMap.get(id);
    }

    @Override
    public Habit update(Habit habit) {
        return habitMap.replace(habit.getId(), habit);
    }

    @Override
    public Habit delete(Habit user) {
        return null;
    }

    @Override
    public List<Habit> findAll() {
        return List.of();
    }

    @Override
    public List<Habit> findByParameters(int id) {
        return List.of();
    }
}