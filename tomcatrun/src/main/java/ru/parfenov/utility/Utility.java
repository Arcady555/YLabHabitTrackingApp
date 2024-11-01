package ru.parfenov.utility;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;

@Slf4j
public class Utility {

    private Utility() {
    }

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

    public static boolean validationEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    public static Role getUserRoleFromString(String str) {
        return ("ADMIN".equals(str)) ? Role.ADMIN : Role.CLIENT;
    }

    public static int getIntFromString(String str) {
        int result = 0;
        try {
            result = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            log.error("Not number for parse from String!", e);
        }
        return result;
    }

    public static long getLongFromString(String str) {
        long result = 0L;
        try {
            result = Long.parseLong(str);
        } catch (NumberFormatException e) {
            log.error("Not number for parse from String!", e);
        }
        return result;
    }

   /* public static Period getPeriodFromString(String str) throws IOException {
        int daysAmount = 0;
        try {
            daysAmount = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            log.error("Please enter the NUMBER!!", e);
        }
        return daysAmount != 0 ? Period.ofDays(daysAmount) : null;
    } */

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
        return Integer.toString( (int) (Math.random() * 10000));
    }

 /*   public static Connection loadConnection() throws ClassNotFoundException, SQLException, IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("homework_2/src/main/resources/app.properties"));
        Connection connection;
        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String driver = prop.getProperty("driver-class-name");
        Class.forName(driver);
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    } */

  /*  public static Optional<User> checkUserByEmailNPass(HttpServletRequest req, UserService userService) {
        Optional<User> result = Optional.empty();
        String authHead = req.getHeader("Authorization");
        if (authHead != null) {
            byte[] e = Base64.decode(authHead.substring(6));
            String emailNPass = new String(e);
            String email = emailNPass.substring(0, emailNPass.indexOf(":"));
            String password = emailNPass.substring(emailNPass.indexOf(":") + 1);
            result = userService.findByEmailAndPassword(email, password);
        }
        return result;
    } */

    public static String getUserEmail(HttpServletRequest req) {
        String result = "";
        String authHead = req.getHeader("Authorization");
        if (authHead != null) {
            byte[] e = Base64.decode(authHead.substring(6));
            String idNPass = new String(e);
            String email = idNPass.substring(0, idNPass.indexOf(":"));
            String password = idNPass.substring(idNPass.indexOf(":") + 1);
        }
        return result;
    }
}