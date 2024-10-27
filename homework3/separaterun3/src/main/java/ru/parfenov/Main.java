package ru.parfenov;

import liquibase.exception.LiquibaseException;

import java.io.IOException;
import java.sql.SQLException;

/**
 * В отдельный блок выведены скрипты liquibase и регулярная рассылка по емайл.
 * Загрузку таблиц в БД можно было сделать через плагин в соседнем блоке.
 * Но тестконтейнеры там же требуют создания объекта liquibase. Тогда плагин будет усложнять задачу.
 */

public class Main {
    public static void main(String[] args) throws SQLException, IOException, LiquibaseException, ClassNotFoundException {
        LiquibaseUpdate liquibaseUpdate = new LiquibaseUpdate();
        liquibaseUpdate.run();

        RegularRequest request = new RegularRequest();
        request.run();
    }
}