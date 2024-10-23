package ru.parfenov.homework_2;

import ru.parfenov.homework_2.server.ServerClass;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("HELLO!!!" + System.lineSeparator() + "Welcome!");
        ServerClass server = new ServerClass();
        server.run();
    }
}