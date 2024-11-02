package ru.parfenov.dto.habit.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.parfenov.dto.habit.HabitCreateDTO;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.dto.habit.HabitStatisticDTO;
import ru.parfenov.dto.habit.HabitUpdateDTO;
import ru.parfenov.model.Habit;

@Mapper
@Component
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