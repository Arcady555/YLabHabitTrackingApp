package ru.parfenov.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.Habit;

import java.time.LocalDate;
import java.util.regex.Pattern;

@Slf4j
public class Utility {

    private Utility() {
    }

    /**
     * Проверка корректного ввода емайла юзером при регистрации
     * @param email строка емайл
     * @return да или нет
     */
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

    /**
     * В случае, если допущена большая просрочка выполнения,
     * придётся накрутить несколько периодов, чтобы выставить корректный срок следующего выполнения
     * (после сегодняшней даты)
     *
     * @param habit Модель ПРИВЫЧКА
     */
    public static void setPlannedNextPerform(Habit habit) {
        LocalDate date = habit.getPlannedPrevPerform() != null ? habit.getPlannedPrevPerform() : habit.getPlannedFirstPerform();
        while (date.isEqual(LocalDate.now())) {
            date = date.plus(habit.getFrequency());
        }
        habit.setPlannedNextPerform(date);
    }

    /**
     * Рандомная генерация кода для значения поля resetPassword в сущности User
     * @return число
     */
    public static String generateForResetPassword() {
        return Integer.toString((int) (Math.random() * 10000));
    }

    /**
     * Получение емайла юзера из его запроса
     * @return строку в его емайлом
     */
    public static String getUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}