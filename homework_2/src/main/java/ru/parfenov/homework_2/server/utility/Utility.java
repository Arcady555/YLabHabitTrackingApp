package ru.parfenov.homework_2.server.utility;

import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_2.server.model.Habit;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Properties;
import java.util.Random;

@Slf4j
public class Utility {
    public static String adminEmail = "admin@YLabHabitApp.com";
    public static String adminPassword = "123";
    /**
     * Сюда вписывается емайл, зарегистрированный на Mail.ru
     */
    public static String emailOfApp = "info@YLabHabitApp.com";
    public static String hostOfApp = "smtp.mail.ru";
    /**
     * Сюда вписывается пароль, сгенерированный в личном кабинете на Mail.ru
     */
    public static String mailPassword = "password";

    private Utility() {
    }

    public static boolean validationEmail(String email) {
        return true;
    }

    public static Period getPeriodFromString(String str) throws IOException {
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

    public static Connection loadConnection() throws ClassNotFoundException, SQLException, IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("homework_2/src/main/resources/db/app.properties"));
        Connection connection;
        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String driver = prop.getProperty("driver-class-name");
        Class.forName(driver);
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }
}