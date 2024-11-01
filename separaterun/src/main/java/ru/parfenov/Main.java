package ru.parfenov;

import liquibase.exception.LiquibaseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * В отдельный блок выведены скрипты liquibase и регулярная рассылка по емайл.
 * Загрузку таблиц в БД можно было сделать через плагин в соседнем блоке.
 * Но тестконтейнеры там же требуют создания объекта liquibase. Тогда плагин будет усложнять задачу.
 */

public class Main {
    public static void main(String[] args) throws LiquibaseException {
        ApplicationContext context = new AnnotationConfigApplicationContext("annotation");

        LiquibaseUpdate liquibaseUpdate = context.getBean(LiquibaseUpdate.class);
        liquibaseUpdate.run();

        RegularRequest request = context.getBean(RegularRequest.class);
        request.run();
    }
}