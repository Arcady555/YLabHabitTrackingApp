package ru.parfenov.homework_1.server.pages.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.pages.UserMenuPage;
import ru.parfenov.homework_1.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UpdateUserPage implements UserMenuPage {
    private final UserService service;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException, InterruptedException {
        System.out.println("Enter the email of the user");
        String email = reader.readLine();
        Optional<User> userOptional = service.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println(user);
            System.out.println("Do you want to delete the user?" + System.lineSeparator() + "0 - yes, another key - no");
            String answerDelete = reader.readLine();
            if (answerDelete.equals("0")) {
                System.out.println(service.delete(user) ? "User is deleted!" : "User is NOT deleted!");
            } else {
                System.out.println(
                        "Do you want to blocK the user?" +
                                System.lineSeparator() +
                                "0 - yes, another key - no");
                String answerBlock = reader.readLine();
                if (answerBlock.equals("0")) {
                    user.setBlocked(true);
                }
                System.out.println(
                        "Do you want to change role (to ADMIN)?" +
                                System.lineSeparator() +
                                "0 - yes, another key - no");
                String answerRole = reader.readLine();
                if (answerRole.equals("0")) {
                    user.setRole(Role.ADMIN);
                }
                System.out.println(
                        "Do you want to change name?" +
                                System.lineSeparator() +
                                "0 - yes, another key - no"
                );
                String answerBrand = reader.readLine();
                if (answerBrand.equals("0")) {
                    System.out.println("Enter new name");
                    String newName = reader.readLine();
                    user.setName(newName);
                }
                System.out.println(
                        "Do you want to change password?" +
                                System.lineSeparator() +
                                "0 - yes, another key - no");
                String answerPassword = reader.readLine();
                if (answerPassword.equals("0")) {
                    System.out.println("Enter new password");
                    String newPassword = reader.readLine();
                    user.setPassword(newPassword);
                }
                System.out.println(service.update(user));
                Thread.sleep(5000);
            }
        } else {
            System.out.println("User not found!");
            run();
        }
    }
}