package ru.parfenov.homework_2.server.email_send.cases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_2.server.email_send.SendViaEmail;
import ru.parfenov.homework_2.server.model.Habit;
import ru.parfenov.homework_2.server.model.User;
import ru.parfenov.homework_2.server.service.HabitService;
import ru.parfenov.homework_2.server.service.UserService;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс формирует данные для отправки сообщений на емайлы юзеров. Ежедневно.
 * С напоминанием, какие привычки им необходимо выполнить сегодня
 */
@Slf4j
@RequiredArgsConstructor
public class RemindViaEmail {
    private final UserService userService;
    private final HabitService habitService;

    public void run() throws MessagingException {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                List<User> userList = userService.findAll();
                for (User user : userList) {
                    List<Habit> todayHabitList = habitService.todayPerforms(user);
                    if (todayHabitList.isEmpty()) {
                        continue;
                    } else {
                        StringBuilder builder = new StringBuilder(
                                        "Hello dear!!!!" +
                                                System.lineSeparator() +
                                                "These are your habits today :" +
                                                System.lineSeparator()
                                );
                        for (Habit habit : todayHabitList) {
                            builder.append(habit.getId()).append(" ").append(habit.getName());
                        }
                        SendViaEmail sendViaEmail = new SendViaEmail(
                                user.getEmail(), "Remind your habit performs today", builder.toString()
                        );
                        try {
                            sendViaEmail.run();
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }, 0, 24 * 60 * 60 * 1000);
    }
}