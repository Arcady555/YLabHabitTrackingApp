package ru.parfenov.utility;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.Habit;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.regex.Pattern;

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
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    public static Role getUserRoleFromString(String str) {
        return "ADMIN".equals(str) ? Role.ADMIN :
                ("CLIENT".equals(str) ? Role.CLIENT :
                        null);
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

    /**
     * В случае, если допущена большая просрочка выполнения,
     * придётся накрутить несколько периодов, чтобы выставить корректный срок следующего выполнения
     * (после сегодняшней даты)
     *
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
        return Integer.toString((int) (Math.random() * 10000));
    }

    public static String getUserEmail(HttpServletRequest req) {
        String email = "";
        String authHead = req.getHeader("Authorization");
        if (authHead != null) {
            byte[] e = Base64.decode(authHead.substring(6));
            String idNPass = new String(e);
            email = idNPass.substring(0, idNPass.indexOf(":"));
        //    String password = idNPass.substring(idNPass.indexOf(":") + 1);
        }
        return email;
    }
}