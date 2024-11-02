package ru.parfenov.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@PropertySource("classpath:application.properties")
@EnableTransactionManagement
public class JdbcConfig {
    @Bean
    public Connection getConnection(
            @Value("${driver-class-name}") String driver,
            @Value("${url}") String url,
            @Value("${username}") String username,
            @Value("${password}") String password
    ) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }
}