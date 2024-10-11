package ru.parfenov.homework_1.server.store;

import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;

import java.util.List;

public interface HabitStore {
    Habit create(Habit habit);

    List<Habit> findByUser(User user);

    Habit findById(long id);

    Habit update(Habit habit);

    Habit delete(Habit habit);

    List<Habit> findAll();

    /**
     * * Метод предполагает поиск по параметрам (всем или некоторые можно не указать)
     * id юзера, его роль, имя, строка(может содержаться в контактной информации), число покупок
     */

    List<Habit> findByParameters(int id);


}