package ru.parfenov.homework_1.server.utility;

import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.model.Habit;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Random;

@Slf4j
public class Utility {
    public static String adminEmail = "admin@habit.ru";
    public static String adminPassword = "123";
    public static String emailOfApp = "info@YLabHabitApp.com";
    public static String hostOfApp = "smtp.mail.ru";
    public static String mailPassword = "kXinQZRNeLj42W29VyK4";

    private Utility() {
    }

    public static boolean validationEmail(String email) {
        return true;
    }

    public static Period enterFrequency(BufferedReader reader) throws IOException {
        String frequencyStr = reader.readLine();
        return getPeriodFromString(frequencyStr);
    }

    public static Period getPeriodFromString(String str) {
        int daysAmount;
        Period frequency = null;
        try {
            daysAmount = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            log.error("Please enter the NUMBER!!", e);
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
    public static void setPlannedNextPerform(Habit habit) {
        LocalDate date = habit.getPlannedPrevPerform() != null ? habit.getPlannedPrevPerform() : habit.getPlannedFirstPerform();
        while (date.isBefore(LocalDate.now())) {
            date = date.plus(habit.getFrequency());
        }
        habit.setPlannedNextPerform(date);
    }

    public static String generateForResetPassword() {
        return new Random(10000).toString();
    }
}