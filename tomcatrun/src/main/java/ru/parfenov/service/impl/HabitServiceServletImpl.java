package ru.parfenov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.dto.habit.*;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.repository.HabitRepository;
import ru.parfenov.service.HabitService;
import ru.parfenov.utility.Utility;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.parfenov.utility.Utility.setPlannedNextPerform;

@Slf4j
@RequiredArgsConstructor
public class HabitServiceServletImpl implements HabitService {
    private final HabitRepository repository;

    @Override
    public Optional<HabitGeneralDTO> create(User user, HabitCreateDTO habitDTO) {
        Optional<HabitGeneralDTO> resultDto = Optional.empty();
        Habit habit = new Habit(
                0L,
                user,
                "true".equals(habitDTO.getUsefulness()),
                true,
                1,
                habitDTO.getName(),
                habitDTO.getDescription(),
                LocalDate.now(),
                LocalDate.parse(habitDTO.getFirstPerform()),
                null,
                LocalDate.parse(habitDTO.getFirstPerform()),
                null,
                Period.ofDays(habitDTO.getFrequency()),
                0
        );
        Optional<Habit> resultOptional = Optional.ofNullable(repository.create(habit));

        if (resultOptional.isPresent()) {
            resultDto = Optional.of(HabitDTOMapper.toHabitGeneralDTO(resultOptional.get()));
        }
        return resultDto;
    }

    @Override
    public boolean delete(long habitId) {
        repository.delete(habitId);
        return findById(habitId).isEmpty();
    }

    @Override
    public boolean deleteWithUser(String userId) {
        int id = Utility.getIntFromString(userId);
        if (id != 0) repository.deleteWithUser(id);

        return repository.findByUser(id).isEmpty();
    }

    @Override
    public Optional<Habit> findById(long id) {
        return Optional.ofNullable(repository.findById(id));
    }

    @Override
    public List<HabitGeneralDTO> findByUser(User user) {
        return executeHabitList(repository.findByUser(user.getId()));
    }

    @Override
    public Optional<HabitGeneralDTO> perform(User user, long habitId) {
        Optional<HabitGeneralDTO> result = Optional.empty();
        Optional<Habit> habitOptional = habitId != 0L ? findById(habitId) : Optional.empty();
        if (habitOptional.isPresent() && validationPerform(user, habitOptional.get())) {
            Habit habit = habitOptional.get();
            LocalDate date = LocalDate.now();

            int streaksAmount = habit.getStreaksAmount();
            /**
             * Если выполнение привычки состоялось после намеченной даты NEXT (опоздало),
             * то количество непрерывных стриков +1
             */
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

            /**
             * После сегодняшнего выполнения количество выполнений привычки +1
             */
            int performsAmount = habit.getPerformsAmount();
            int newPerformsAmount = ++performsAmount;
            habit.setPerformsAmount(newPerformsAmount);
            Habit newHabit = repository.updateViaPerform(habit);
            result = habit.getStreaksAmount() == newHabit.getStreaksAmount() &&
                    habit.getPlannedNextPerform().isEqual(newHabit.getPlannedNextPerform()) ?
                    Optional.of(HabitDTOMapper.toHabitGeneralDTO(newHabit)) :
                    Optional.empty();
        }
        return result;
    }

    @Override
    public Optional<HabitGeneralDTO> updateByUser(User user, HabitUpdateDTO habitDTO) {
        String usefulness = habitDTO.getUsefulness();
        String active = habitDTO.getActive();
        String name = habitDTO.getName();
        String description = habitDTO.getDescription();
        int frequency = habitDTO.getFrequency();
        Habit habit = repository.findById(habitDTO.getHabitId());
        if (habit != null && habit.getUser().equals(user)) {
            habit = repository.updateByUser(
                    habitDTO.getHabitId(),
                    habitDTO.getUsefulness(),
                    habitDTO.getActive(),
                    habitDTO.getName(),
                    habitDTO.getDescription(),
                    habitDTO.getFrequency()
            );
        }
        return habit != null && checkUpdate(habit, usefulness, active, name, description, frequency) ?
                Optional.of(HabitDTOMapper.toHabitGeneralDTO(habit)) :
                Optional.empty();
    }

    @Override
    public List<HabitGeneralDTO> todayPerforms(User user) {
        return executeHabitList(repository.findByUserForToday(user));
    }

    @Override
    public List<HabitStatisticDTO> statisticForUser(User user, String dateFromStr, String dateToStr) {
        List<HabitStatisticDTO> result = new ArrayList<>();
        LocalDate dateFrom = LocalDate.parse(dateFromStr);
        LocalDate dateTo = LocalDate.parse(dateToStr);
        List<Habit> habits = repository.findByUser(user.getId());
        for (Habit habit : habits) {
            HabitStatisticDTO habitDTO = HabitDTOMapper.toHabitStatisticDTO(habit);
            habitDTO.setStatistic(statistic(habit.getId(), dateFrom, dateTo));
            result.add(habitDTO);
        }
        return result;
    }

    @Override
    public List<HabitGeneralDTO> findByParameters(
            User user,
            String usefulness,
            String active,
            String name,
            String description,
            String dateOfCreate,
            String frequency
    ) {
        return executeHabitList(repository.findByParameters(user, usefulness, active, name, description, dateOfCreate, frequency));
    }

    /**
     * Проверяется, что привычка принадлежит юзеру.
     * И выполнение привычки невозможно раньше намеченного срока
     * Эта же валидация не даст выполнить привычку 2раза подряд (когда в методе perform() next станет prev)
     *
     * @param habit Модель ПРИВЫЧКА
     * @return true или false
     */
    private boolean validationPerform(User user, Habit habit) {
        boolean check1 = user.equals(habit.getUser());
        LocalDate date =
                habit.getPlannedPrevPerform() == null ?
                        habit.getPlannedFirstPerform() :
                        habit.getPlannedPrevPerform();
        boolean check2 = LocalDate.now().isAfter(date) || LocalDate.now().isEqual(date);
        return check1 && check2;
    }

    private boolean checkUpdate(Habit habit, String newUsefulness, String newActive, String newName, String newDescription, int newFrequency) {
        boolean check1 = newUsefulness.isEmpty() ||
                (newUsefulness.equals("true") && habit.isUseful()) ||
                (newUsefulness.equals("false") && !habit.isUseful());
        boolean check2 = newActive.isEmpty() ||
                (newActive.equals("true") && habit.isActive()) ||
                (newActive.equals("false") && !habit.isActive());
        boolean check3 = newName.isEmpty() || habit.getName().equals(newName);
        boolean check4 = newDescription.isEmpty() || habit.getName().equals(newDescription);
        boolean check5 = newFrequency == 0 || habit.getFrequency().equals(Period.ofDays(newFrequency));

        return check1 && check2 && check3 && check4 && check5;
    }

    private List<HabitGeneralDTO> executeHabitList(List<Habit> list) {
        List<HabitGeneralDTO> result = new ArrayList<>();
        for (Habit habit : list) {
            HabitGeneralDTO habitDTO = HabitDTOMapper.toHabitGeneralDTO(habit);
            habitDTO.setRemind(remind(habit.getId()));
            result.add(habitDTO);
        }
        return result;
    }

    private String remind(long habitId) {
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

    /**
     * Статистика выполнения привычки. Срок выставляется пользователем.
     * Количество периодов за данный срок сравнивается с количеством выполнений(habit.getPerformsAmount())
     *
     * @param habitId  ID привычки
     * @param dateFrom Начальная дата выборки
     * @param dateTo   Конечная дата выборки
     * @return Текст с полученными данными
     */
    private String statistic(long habitId, LocalDate dateFrom, LocalDate dateTo) {
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
}