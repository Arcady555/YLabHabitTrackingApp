package ru.parfenov.dto.habit.mapper;

import org.mapstruct.Mapper;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.dto.habit.HabitStatisticDTO;
import ru.parfenov.model.Habit;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface HabitDTOMapper {
    /**
     * Перевод Habit сущности в DTO, с исходными полями, переведёнными в int и String.
     * (все поля становятся или int или String)
     * Применяется при распечатке данных привычки в списке со статистикой
     *
     * @param source Habit сущность
     * @return DTO объект
     */
    default HabitStatisticDTO toHabitStatisticDTO(Habit source) {
        return new HabitStatisticDTO(
                source.getId(),
                source.isUseful() ? "true" : "false",
                source.getName(),
                source.getDescription(),
                source.getDateOfCreate().toString(),
                source.getPlannedNextPerform().toString(),
                source.getLastRealPerform() != null ? source.getLastRealPerform().toString() : null,
                source.getFrequency().getDays(),
                ""
        );
    }

    /**
     * Перевод Habit сущности в DTO, со всеми исходными полями, переведёнными в int и String.
     * (все поля становятся или int или String)
     * Применяется при распечатке данных привычки в списке
     *
     * @param source
     * @return
     */
    default HabitGeneralDTO toHabitGeneralDTO(Habit source) {
        return new HabitGeneralDTO(
                source.getId(),
                source.getUser().getEmail(),
                source.isUseful(),
                source.isActive(),
                source.getStreaksAmount(),
                source.getName(),
                source.getDescription(),
                source.getDateOfCreate().toString(),
                source.getPlannedNextPerform().toString(),
                source.getLastRealPerform() != null ? source.getLastRealPerform().toString() : null,
                source.getFrequency().getDays(),
                source.getPerformsAmount(),
                ""
        );
    }

    /**
     * Перевод списка с Habit в список с HabitGeneralDTO.
     * Применяется при распечатке данных
     *
     * @param source список сущностей Habit
     * @return список объектов DTO
     */
    default List<HabitGeneralDTO> toHabitGeneralDTOList(List<Habit> source) {
        List<HabitGeneralDTO> result = new ArrayList<>();
        for (Habit habit : source) {
            HabitGeneralDTO habitDTO = toHabitGeneralDTO(habit);
            result.add(habitDTO);
        }
        return result;
    }
}