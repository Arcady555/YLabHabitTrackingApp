package ru.parfenov.server.pages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static ru.parfenov.server.utility.Utility.validationEmail;

/**
 * Страница регистрации пользователя.
 */
@Slf4j
@RequiredArgsConstructor
public class SignUpPage {
    private final UserService service;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void run() throws IOException {
        System.out.println("Enter Your name (or exit)");
        String name = reader.readLine();
        if (name.isEmpty()) {
           name = "no name";
        }
        if (name.equals("exit")) return;
        System.out.println("Enter Your email");
        String email = reader.readLine();
        if (email.isEmpty()) {
            System.out.println("no email!!!");
            run();
        }
        if (!validationEmail(email)) {
            System.out.println("not correct email!");
            run();
        }
        if (service.findByEmail(email).isPresent()) {
            System.out.println("User with the email is already exist!");
            run();
        } else {
            System.out.println("Enter password");
            String password = reader.readLine();
            if (password.isEmpty()) {
                System.out.println("No password!");
                run();
            } else {
                System.out.println(service.createByReg(email, password, name));
            }
        }
    }
}