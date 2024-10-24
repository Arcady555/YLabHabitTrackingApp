package ru.parfenov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.repository.HabitRepository;
import ru.parfenov.service.HabitService;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static ru.parfenov.utility.Utility.setPlannedNextPerform;

@Slf4j
@RequiredArgsConstructor
public class HabitServiceConsoleImpl implements HabitService {
    private final HabitRepository repository;

    @Override
    public Optional<Habit> create(
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
                1,
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
        return Optional.ofNullable(repository.create(habit));
    }

    @Override
    public boolean delete(long habitId) {
        repository.delete(habitId);
        return findById(habitId).isEmpty();
    }

    @Override
    public void deleteWithUser(User user) {
        repository.deleteWithUser(user.getId());
    }

    @Override
    public Optional<Habit> findById(long id) {
        return Optional.ofNullable(repository.findById(id));
    }

    public List<Habit> findByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public boolean perform(Habit habit) {
        boolean result = false;
        if (!validationPerform(habit)) {
            LocalDate date = LocalDate.now();

            int streaksAmount = habit.getStreaksAmount();
            if (date.isAfter(habit.getPlannedNextPerform())) {
                streaksAmount++;
                habit.setStreaksAmount(streaksAmount);
            }
            habit.setLastRealPerform(date);

            habit.setPlannedPrevPerform(habit.getPlannedNextPerform());
            /**
             * В случае, если допущена большая просрочка выполнения,
             * придётся накрутить несколько периодов, чтобы выставить корректный срок следующего выполнения
             * (после сегодняшней даты)
             */
            setPlannedNextPerform(habit);

            int performsAmount = habit.getPerformsAmount();
            int newPerformsAmount = ++performsAmount;
            habit.setPerformsAmount(newPerformsAmount);
            Habit newHabit = repository.updateViaPerform(habit);
            result = habit.getStreaksAmount() == newHabit.getStreaksAmount() &&
                    habit.getPlannedNextPerform().isEqual(newHabit.getPlannedNextPerform());
        }
        return result;
    }

    @Override
    public Habit updateByUser(long habitId, String newUsefulness, String newActive, String newName, String newDescription, String newFrequency) {
        return repository.updateByUser(habitId, newUsefulness, newActive, newName, newDescription, newFrequency);
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
        return repository.findByUserForToday(user);
    }

    /**
     * Статистика выполнения привычки. Срок выставляется пользователем.
     * Количество периодов за данный срок сравнивается с количеством выполнений(habit.getPerformsAmount())
     *
     * @param habitId  ID привычки
     * @param dateFrom Начальная дата выборки
     * @param dateTo   Конечная дата выборки
     * @return Текст с полученными данными
     */
    @Override
    public String statistic(long habitId, LocalDate dateFrom, LocalDate dateTo) {
        String result;
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
                        if (habit.getPerformsAmount() != 0) {
                            float realPercent = (float) hundredPercent / habit.getPerformsAmount() * 100;
                            result = "From " +
                                    dateFrom +
                                    " to " +
                                    dateTo +
                                    ", the habit was completed by " +
                                    realPercent +
                                    "%." +
                                    System.lineSeparator() +
                                    "And during all time of the habit there were " +
                                    (habit.getStreaksAmount() - 1) +
                                    "cases of going beyond the execution schedule.";
                        } else {
                            result = "The habit is no completed yet";
                        }
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

    public List<Habit> findByParameters(
            User user,
            String usefulness,
            String active,
            String name,
            String description,
            String dateOfCreate,
            String frequency
    ) {
        return repository.findByParameters(user, usefulness, active, name, description, dateOfCreate, frequency);
    }

    /**
     * Выполнение привычки невозможно раньше намеченного срока
     * Эта же валидация не даст выполнить привычку 2раза подряд (когда в методе perform() next станет prev)
     *
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