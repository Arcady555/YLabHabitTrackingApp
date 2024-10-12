package ru.parfenov.homework_1.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.HabitService;
import ru.parfenov.homework_1.server.store.HabitStore;
import ru.parfenov.homework_1.server.utility.Utility;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class HabitServiceConsoleImpl implements HabitService {
    private final HabitStore habitStore;

    @Override
    public Habit create(
            User user,
            boolean usefulness,
            String name,
            String description,
            LocalDate dateOfCreate,
            LocalDate firstPerform,
            Period frequency
    ) {
        Habit habit = new Habit(
                0L,
                user,
                usefulness,
                true,
                name,
                description,
                dateOfCreate,
                firstPerform,
                null,
                firstPerform,
                null,
                frequency,
                0
        );
        habitStore.create(habit);
        return habit;
    }

    @Override
    public Optional<Habit> findById(long id) {
        return Optional.ofNullable(habitStore.findById(id));
    }

    public List<Habit> findByUser(User user) {
        return habitStore.findByUser(user);
    }

    @Override
    public boolean perform(Habit habit) {
        boolean result = false;
        if (!validationPerform(habit)) {
            LocalDate date = LocalDate.now();
            habit.setLastRealPerform(date);

            habit.setPlannedPrevPerform(habit.getPlannedNextPerform());
            /**
             * В случае, если допущена большая просрочка выполнения,
             * придётся накрутить несколько периодов, чтобы выставить корректный срок следующего выполнения
             * (после сегодняшней даты)
             */
            habit.setPlannedNextPerform(Utility.setPlannedNextPerform(habit));

            int performsAmount = habit.getPerformsAmount();
            int newPerformsAmount = ++performsAmount;
            habit.setPerformsAmount(newPerformsAmount);

            habitStore.update(habit);
            result = habitStore.findById(habit.getId()).getLastRealPerform().isEqual(date)
                    &&
                    habitStore.findById(habit.getId()).getPerformsAmount() == newPerformsAmount;
        }
        return  result;
    }

    @Override
    public Habit update(Habit habit) {
        return null;
    }

    @Override
    public String remind(long habitId) {
        String result = "The habit is absent or not active. No remind";
        Optional<Habit> habitOptional = findById(habitId);
        if (habitOptional.isPresent()) {
            Habit habit = habitOptional.get();
            if (habit.isActive()) {
                int daysAmount = (int) ChronoUnit.DAYS.between(LocalDate.now(), habit.getPlannedNextPerform());
                if (daysAmount > 0) {
                    result = "Perform the habit in " + daysAmount + " days";
                } else if (daysAmount == 0) {
                    result = "Perform the habit today!";
                } else {
                    result = "Perform of the habit is " + Math.abs(daysAmount) + " days overdue!!!";
                }
            }
        }
        return result;
    }

    @Override
    public List<Habit> todayPerforms(User user) {
        List<Habit> list = habitStore.findByUser(user);
        List<Habit> result = new ArrayList<>();
        for (Habit habit : list) {
            if (habit.getPlannedNextPerform().isEqual(LocalDate.now()) ||
                    habit.getPlannedNextPerform().isBefore(LocalDate.now()))
            {
                result.add(habit);
            }
        }
        return result;
    }

    /**
     * Статистика выполнения привычки. Срок выставляется пользователем.
     * Количество периодов за данный срок сравнивается с количеством выполнений(habit.getPerformsAmount())
     * @param habitId  ID привычки
     * @param dateFrom Начальная дата выборки
     * @param dateTo Конечная дата выборки
     * @return Текст с полученными данными
     */
    @Override
    public String statistic(long habitId, LocalDate dateFrom, LocalDate dateTo) {
        String result = "";
        if (dateTo.isBefore(dateFrom) || dateTo.isEqual(dateFrom) || dateFrom.isAfter(LocalDate.now())) {
            result = "Not correct date!";
        } else {
            Optional<Habit> habitOptional = findById(habitId);
            if (habitOptional.isPresent()) {
                Habit habit = habitOptional.get();
                if (habit.isActive()) {
                    if (
                            (dateFrom.isAfter(habit.getDateOfCreate()) || dateFrom.isEqual(habit.getDateOfCreate()))
                                    &&
                                    (dateTo.isBefore(LocalDate.now()) || dateTo.isEqual(LocalDate.now()))
                    ) {
                        int period = (int) ChronoUnit.DAYS.between(dateFrom, dateTo);
                        int hundredPercent = period / habit.getFrequency().getDays();
                        float realPercent = (float) hundredPercent / habit.getPerformsAmount();
                        result = "From " + dateFrom + " to " + dateTo + ", the habit was completed by " + realPercent;
                    } else {
                        result = "Not correct date!";
                    }
                } else {
                    result = "The habit is not active. No statistic";
                }
            } else {
                result = "The habit is not exist. No statistic";
            }
        }
        return result;
    }

    /**
     * Выполнение привычки невозможно раньше намеченного срока
     * Эта же валидация не даст выполнить привычку 2раза подряд (когда в методе perform() next станет prev)
     * @param habit Модель ПРИВЫЧКА
     * @return true или false
     */
    private boolean validationPerform(Habit habit) {
        LocalDate date =
                habit.getPlannedPrevPerform() == null ?
                        habit.getPlannedFirstPerform() :
                        habit.getPlannedPrevPerform();
        return LocalDate.now().isAfter(date) || LocalDate.now().isEqual(date);

    }
}