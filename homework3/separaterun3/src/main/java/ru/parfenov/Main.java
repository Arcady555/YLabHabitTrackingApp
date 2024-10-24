package ru.parfenov;

import liquibase.exception.LiquibaseException;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, LiquibaseException, ClassNotFoundException {
        LiquibaseUpdate liquibaseUpdate = new LiquibaseUpdate();
        liquibaseUpdate.run();

    }
}