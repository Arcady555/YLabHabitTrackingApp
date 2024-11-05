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
@PropertySource("classpath:app.properties")
@EnableTransactionManagement
public class JdbcConfig {
    private @Value("${driver-class-name}") String driver;
    private @Value("${url}") String url;
    private @Value("${username}") String username;
    private @Value("${password}") String password;

    @Bean
    public Connection getConnection()throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }
}