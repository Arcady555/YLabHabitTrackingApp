package ru.parfenov.emailsend.cases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.parfenov.emailsend.SendViaEmail;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.repository.HabitRepository;
import ru.parfenov.service.UserService;

import javax.mail.MessagingException;
import java.util.List;

/**
 * Класс формирует данные для отправки сообщений на емайлы юзеров. Ежедневно.
 * С напоминанием, какие привычки им необходимо выполнить сегодня
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RemindViaEmail {
    private final UserService userService;
    private final HabitRepository habitRepository;

    public void run() throws MessagingException {
        List<User> userList = userService.findAllForMail();
        for (User user : userList) {
            List<Habit> todayHabitList = habitRepository.findByUserForToday(user);
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
}