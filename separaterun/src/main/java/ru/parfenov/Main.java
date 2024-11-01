package ru.parfenov;

import liquibase.exception.LiquibaseException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * В отдельный блок выведен регулярный запрос на рассылку по емайл.
 */

public class Main {
    public static void main(String[] args) throws LiquibaseException {
        ApplicationContext context = new AnnotationConfigApplicationContext("ru.parfenov");

        RegularRequest request = context.getBean(RegularRequest.class);
        request.run();
    }
}