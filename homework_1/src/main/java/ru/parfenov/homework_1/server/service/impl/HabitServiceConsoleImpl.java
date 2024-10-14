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
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.parfenov.homework_1.server.utility.Utility.setPlannedNextPerform;

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
        return habitStore.create(habit);
    }

    @Override
    public boolean delete(long habitId) {
        return habitStore.delete(habitId) != null;
    }

    @Override
    public void deleteByUser(User user) {
        List<Habit> list = findByUser(user);
        for (Habit habit : list) {
            if (habit.getUser().equals(user)) {
                delete(habit.getId());
            }
        }
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

            result = habitStore.update(habit) == habit;
        }
        return  result;
    }

    @Override
    public boolean update(Habit habit, boolean usefulness, boolean active, String name, String description, Period frequency) {
        habit.setUseful(usefulness);
        habit.setActive(active);
        habit.setName(name);
        habit.setDescription(description);
        habit.setFrequency(frequency);
        setPlannedNextPerform(habit);
        return habitStore.update(habit) == habit;
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
            String  name,
            String description,
            String dateOfCreate,
            String frequency
    ) {
        List<Habit> result = new ArrayList<>();
        for (Habit habit : findByUser(user)) {
            if (select (habit, usefulness, active, name, description, dateOfCreate, frequency)) {
                result.add(habit);
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

    private boolean select(Habit habit,
                           String usefulness,
                           String active,
                           String  name,
                           String description,
                           String dateOfCreate,
                           String frequency) {
        boolean check1 = usefulness.isEmpty() ||
                (usefulness.equals("true") && habit.isUseful()) ||
                (usefulness.equals("false") && !habit.isUseful());
        boolean check2 = active.isEmpty() ||
                (active.equals("true") && habit.isActive()) ||
                (active.equals("false") && !habit.isActive());
        boolean check3 = name.isEmpty() || habit.getName().contains(name);
        boolean check4 = description.isEmpty() || habit.getDescription().contains(description);
        boolean check5 = dateOfCreate.isEmpty() || checkDateOfCreate(habit.getDateOfCreate(), dateOfCreate);
        boolean check6 = frequency.isEmpty() || checkFrequency(habit.getFrequency(), frequency);

        return check1 && check2 && check3 && check4 && check5 && check6;
    }

    private boolean checkDateOfCreate(LocalDate habitDateOfCreate, String dateOfCreateStr) {
        boolean result = false;
        LocalDate localDate;
            try {
                localDate = LocalDate.parse(dateOfCreateStr);
                result = localDate.isEqual(habitDateOfCreate);
            } catch (DateTimeParseException e) {
                log.error("Please enter correct format!", e);
            }
        return result;
    }

    private boolean checkFrequency(Period habitPeriod, String periodStr) {
        Period period = Utility.getPeriodFromString(periodStr);
        return habitPeriod.equals(period);
    }
}