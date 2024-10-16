package ru.parfenov.homework_1;

import ru.parfenov.homework_1.server.ServerClass;

import javax.mail.MessagingException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException, MessagingException {
        System.out.println("HELLO!!!" + System.lineSeparator() + "Welcome!");
        ServerClass server = new ServerClass();
        server.run();
    }
}