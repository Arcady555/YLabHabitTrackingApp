package ru.parfenov.converter;

import jakarta.persistence.AttributeConverter;

import java.time.Period;

/**
 * Данный класс служит для корректной трансформации поля frequency(тип java.time.Period) в сущности Habit
 * при работе c БД (через CrudRepository)
 */
public class PeriodIntConverter implements AttributeConverter<Period, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Period period) {
        return period.getDays();
    }

    @Override
    public Period convertToEntityAttribute(Integer integer) {
        return Period.ofDays(integer);
    }
}