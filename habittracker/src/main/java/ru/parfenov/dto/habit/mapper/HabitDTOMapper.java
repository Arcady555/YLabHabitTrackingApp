package ru.parfenov.dto.habit.mapper;

import org.mapstruct.Mapper;
import ru.parfenov.dto.habit.HabitCreateDTO;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.dto.habit.HabitStatisticDTO;
import ru.parfenov.dto.habit.HabitUpdateDTO;
import ru.parfenov.model.Habit;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface HabitDTOMapper {

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

    default List<HabitGeneralDTO> toHabitGeneralDTOList(List<Habit> source) {
        List<HabitGeneralDTO> result = new ArrayList<>();
        for (Habit habit : source) {
            HabitGeneralDTO habitDTO = toHabitGeneralDTO(habit);
            result.add(habitDTO);
        }
        return result;
    }

    default HabitCreateDTO toHabitCreateDTO(Habit source) {
        return new HabitCreateDTO(
                source.isUseful() ? "true" : "false",
                source.getName(),
                source.getDescription(),
                source.getPlannedFirstPerform().toString(),
                source.getFrequency().getDays()
        );
    }

    default HabitUpdateDTO toHabitUpdateDTO(Habit source) {
        return new HabitUpdateDTO(
                source.getId(),
                source.isUseful() ? "true" : "false",
                source.isActive() ? "true" : "false",
                source.getName(),
                source.getDescription(),
                source.getFrequency().getDays()
        );
    }
}