package ru.parfenov.homework_1.server.utility;

import ru.parfenov.homework_1.server.model.Habit;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

public class Utility {
    public static String adminEmail = "admin@habit.ru";
    public static String adminPassword = "123";

    private Utility() {
    }

    public static boolean validationEmail(String email) {
        return true;
    }

    public static Period enterFrequency(BufferedReader reader) {
        int daysAmount = 0;
        Period frequency = null;
        try {
            daysAmount = Integer.parseInt(reader.readLine());
        } catch (NumberFormatException | IOException e) {
            System.out.println("Please enter the NUMBER!");
            return frequency;
        }
        if (daysAmount < 1 || daysAmount > 90) {
            System.out.println("Please enter correct!");
            return frequency;
        }
        if (daysAmount % 30 == 0) {
            frequency = Period.ofMonths(daysAmount / 30);
        } else {
            frequency = Period.of(0, daysAmount / 30, daysAmount % 30);
        }
        return frequency;
    }

    /**
     * В случае, если допущена большая просрочка выполнения,
     * придётся накрутить несколько периодов, чтобы выставить корректный срок следующего выполнения
     * (после сегодняшней даты)
     * @param habit Модель ПРИВЫЧКА
     * @return дату, которую можно засетить
     */
    public static LocalDate setPlannedNextPerform(Habit habit) {
        LocalDate date = habit.getPlannedPrevPerform();
        while (date.isBefore(LocalDate.now())) {
            date = date.plus(habit.getFrequency());
        }
        return date;
    }
}