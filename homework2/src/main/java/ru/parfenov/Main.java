package ru.parfenov;

import ru.parfenov.server.ServerClass;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("HELLO!!!" + System.lineSeparator() + "Welcome!");
        ServerClass server = new ServerClass();
        server.run();
    }
}