package ru.parfenov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.parfenov.dto.habit.HabitCreateDTO;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.dto.habit.HabitStatisticDTO;
import ru.parfenov.dto.habit.HabitUpdateDTO;
import ru.parfenov.dto.habit.mapper.HabitDTOMapper;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.repository.HabitRepository;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;
import ru.parfenov.utility.Utility;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.parfenov.utility.Utility.setPlannedNextPerform;

@Slf4j
@Service
@RequiredArgsConstructor
public class HabitServiceSpringImpl implements HabitService {
    private final HabitRepository repository;
    private final UserService userService;
    private final HabitDTOMapper dtoMapper;

    @Override
    public Optional<HabitGeneralDTO> create(HabitCreateDTO habitDTO) {
        Optional<HabitGeneralDTO> resultDto = Optional.empty();
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail());
        if (userOptional.isPresent()) {
            Habit habit = new Habit(
                    0L,
                    userOptional.get(),
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

            Habit result = repository.save(habit);
            System.out.println("finish");
            if (findById(result.getId()).isPresent()) {
                resultDto = Optional.of(dtoMapper.toHabitGeneralDTO(result));
            }
        }
        return resultDto;
    }

    @Override
    public boolean delete(long habitId) {
        boolean result = false;
        Optional<Habit> habitOptional = findById(habitId);
        if (habitOptional.isPresent()) {
            repository.delete(habitOptional.get());
            result = findById(habitId).isEmpty();
        }
        return result;
    }

    @Override
    @Transactional
    public boolean deleteWithUser(int userId) {
        repository.deleteWithUser(userId);
        return true;
    }

    @Override
    public Optional<Habit> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public List<HabitGeneralDTO> findByUser() {
        List<HabitGeneralDTO> result = Collections.emptyList();
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail());
        if (userOptional.isPresent()) {
            result = dtoMapper.toHabitGeneralDTOList(repository.findByUser(userOptional.get()));
            for (HabitGeneralDTO habit : result) {
                habit.setRemind(remind(habit.getId()));
            }
        }
        return result;
    }

    @Override
    public Optional<HabitGeneralDTO> perform(long habitId) {
        Optional<HabitGeneralDTO> result = Optional.empty();
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail());
        if (userOptional.isPresent()) {
            Optional<Habit> habitOptional = habitId != 0L ? findById(habitId) : Optional.empty();
            if (habitOptional.isPresent() && validationPerform(userOptional.get(), habitOptional.get())) {
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
                Habit newHabit = repository.save(habit);
                result = habit.getStreaksAmount() == newHabit.getStreaksAmount() &&
                        habit.getPlannedNextPerform().isEqual(newHabit.getPlannedNextPerform()) ?
                        Optional.of(dtoMapper.toHabitGeneralDTO(newHabit)) :
                        Optional.empty();
            }
        }
        return result;
    }

    @Override
    public Optional<HabitGeneralDTO> updateByUser(HabitUpdateDTO habitDTO) {
        Optional<HabitGeneralDTO> result = Optional.empty();
        Habit newHabit = null;
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail());
        if (userOptional.isPresent()) {
            long habitId = habitDTO.getHabitId();
            String newUsefulness = habitDTO.getUsefulness();
            String newActive = habitDTO.getActive();
            String newName = habitDTO.getName();
            String newDescription = habitDTO.getDescription();
            int newFrequency = habitDTO.getFrequency();

            Optional<Habit> habitOptional = findById(habitId);

            if (habitOptional.isPresent()) {
                Habit habit = habitOptional.get();
                if (habit.getUser().equals(userOptional.get())) {
                    if (!newUsefulness.isEmpty()) habit.setUseful("true".equals(newUsefulness));
                    if (!newActive.isEmpty()) habit.setActive("true".equals(newActive));
                    if (!newName.isEmpty()) habit.setName(newName);
                    if (!newDescription.isEmpty()) habit.setDescription(newDescription);
                    if (newFrequency != 0) habit.setFrequency(Period.ofDays(newFrequency));
                    newHabit = repository.save(habit);
                }
            }
            result = newHabit != null &&
                    findById(newHabit.getId()).isPresent() &&
                    checkUpdate(newHabit, newUsefulness, newActive, newName, newDescription, newFrequency) ?
                    Optional.of(dtoMapper.toHabitGeneralDTO(newHabit)) :
                    Optional.empty();
        }
        return result;
    }

    @Override
    public List<HabitGeneralDTO> todayPerforms() {
        List<HabitGeneralDTO> result = Collections.emptyList();
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail());
        if (userOptional.isPresent()) {
            result = dtoMapper.toHabitGeneralDTOList(repository.findByUserForToday(userOptional.get().getId()));
            for (HabitGeneralDTO habit : result) {
                habit.setRemind(remind(habit.getId()));
            }
        }
        return result;
    }

    @Override
    public List<HabitStatisticDTO> statisticForUser(String dateFromStr, String dateToStr) {
        List<HabitStatisticDTO> result = new ArrayList<>();
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail());
        if (userOptional.isPresent()) {
            LocalDate dateFrom = LocalDate.parse(dateFromStr);
            LocalDate dateTo = LocalDate.parse(dateToStr);
            List<Habit> habits = repository.findByUser(userOptional.get());
            for (Habit habit : habits) {
                HabitStatisticDTO habitDTO = dtoMapper.toHabitStatisticDTO(habit);
                habitDTO.setStatistic(statistic(habit.getId(), dateFrom, dateTo));
                result.add(habitDTO);
            }
        }
        return result;
    }

    @Override
    public List<HabitGeneralDTO> findByParameters(
            String usefulnessStr,
            String activeStr,
            String name,
            String description,
            String dateOfCreateStr,
            int frequencyInt
    ) {
        List<HabitGeneralDTO> result = Collections.emptyList();
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail());
        if (userOptional.isPresent()) {
            boolean usefulness = "true".equals(usefulnessStr);
            boolean active = "true".equals(activeStr);
            LocalDate dateOfCreate = LocalDate.parse(dateOfCreateStr);
            Period frequency = Period.ofDays(frequencyInt);
            result = dtoMapper.toHabitGeneralDTOList(
                    repository.findByParameters(
                            userOptional.get().getId(),
                            usefulnessStr,
                            usefulness,
                            activeStr,
                            active,
                            name,
                            description,
                            dateOfCreate,
                            frequency)
            );
            for (HabitGeneralDTO habit : result) {
                habit.setRemind(remind(habit.getId()));
            }
        }
        return result;
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
     * Количество периодов за данный срок сравнивается с количеством фактических выполнений(habit.getPerformsAmount())
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