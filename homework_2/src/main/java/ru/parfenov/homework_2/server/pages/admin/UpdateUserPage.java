package ru.parfenov.homework_2.server.pages.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_2.server.enums.user.Role;
import ru.parfenov.homework_2.server.model.User;
import ru.parfenov.homework_2.server.pages.UserMenuPage;
import ru.parfenov.homework_2.server.service.HabitService;
import ru.parfenov.homework_2.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * Страница, где админ может подкорректировать данные по юзеру
 * (кроме емайл, ведь это уникальный параметр, как идентификатор)
 */
@Slf4j
@RequiredArgsConstructor
public class UpdateUserPage implements UserMenuPage {
    private final UserService userService;
    private final HabitService habitService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException {
        System.out.println("Enter the email of the user");
        String email = reader.readLine();
        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println(user);
            System.out.println("Do you want to delete the user?" + System.lineSeparator() + "0 - yes, another key - no");
            String answerDelete = reader.readLine();
            if (answerDelete.equals("0")) {
                habitService.deleteWithUser(user);
                System.out.println(userService.delete(user) ? "User is deleted!" : "User is NOT deleted!");
            } else {
                String newPassword = "";
                String newName = "";
                Role newUserRole = null;
                String blockStr = "";
                System.out.println(
                        "Do you want to change block(or not block) of the user?" +
                                System.lineSeparator() +
                                "0 - yes, another key - no");
                if (reader.readLine().equals("0")) {
                    blockStr = user.isBlocked() ? "false" : "true";
                }
                System.out.println(
                        "Do you want to change role?" +
                                System.lineSeparator() +
                                "0 - yes, another key - no");
                if (reader.readLine().equals("0")) {
                    newUserRole = user.getRole() == Role.ADMIN ? Role.CLIENT : Role.ADMIN;
                }
                System.out.println(
                        "Do you want to change name?" +
                                System.lineSeparator() +
                                "0 - yes, another key - no"
                );
                if (reader.readLine().equals("0")) {
                    System.out.println("Enter new name");
                    newName = reader.readLine();
                }
                System.out.println(
                        "Do you want to change password?" +
                                System.lineSeparator() +
                                "0 - yes, another key - no");
                if (reader.readLine().equals("0")) {
                    System.out.println("Enter new password");
                    newPassword = reader.readLine();
                }
                System.out.println(userService.update(user.getId(), newPassword, "", newName, newUserRole, blockStr));
            }
        } else {
            System.out.println("User not found!");
            run();
        }
    }
}