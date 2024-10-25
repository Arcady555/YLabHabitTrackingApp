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
                source.getLastRealPerform().toString(),
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
                source.getLastRealPerform().toString(),
                source.getFrequency().getDays(),
                source.getPerformsAmount(),
                ""
        );
    }
}