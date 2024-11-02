package ru.parfenov.dto.habit;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import ru.parfenov.model.Habit;

@Mapper
public interface HabitDTOMapper {
    @Named("toHabitStatisticDTO")
    static HabitStatisticDTO toHabitStatisticDTO(Habit source) {
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

    @Named("toHabitGeneralDTO")
    static HabitGeneralDTO toHabitGeneralDTO(Habit source) {
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

    @Named("toHabitCreateDTO")
    static HabitCreateDTO toHabitCreateDTO(Habit source) {
        return new HabitCreateDTO(
                source.isUseful() ? "true" : "false",
                source.getName(),
                source.getDescription(),
                source.getPlannedFirstPerform().toString(),
                source.getFrequency().getDays()
        );
    }

    @Named("toHabitUpdateDTO")
    static HabitUpdateDTO toHabitUpdateDTO(Habit source) {
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