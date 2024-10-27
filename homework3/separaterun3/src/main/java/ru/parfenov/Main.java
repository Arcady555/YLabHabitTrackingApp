package ru.parfenov;

import liquibase.exception.LiquibaseException;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, LiquibaseException, ClassNotFoundException {
        LiquibaseUpdate liquibaseUpdate = new LiquibaseUpdate();
        liquibaseUpdate.run();

        RegularRequest request = new RegularRequest();
        request.run();
    }
}