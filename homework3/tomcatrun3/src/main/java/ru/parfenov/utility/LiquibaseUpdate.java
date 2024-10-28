package ru.parfenov.utility;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LiquibaseUpdate {
    private final Connection connection; // = loadConnection();

    public LiquibaseUpdate(Connection connection) throws SQLException, IOException, ClassNotFoundException {
        this.connection = connection;
    }

    public void run() throws LiquibaseException {
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

        Liquibase liquibase = new Liquibase("db/changelog.xml", new ClassLoaderResourceAccessor(), database);

        liquibase.update(new Contexts(), new LabelExpression());
    }

 /*   private Connection loadConnection() throws ClassNotFoundException, SQLException, IOException {
        Properties prop = new Properties();
      //  prop.load(new FileInputStream("homework3/separaterun3/src/main/resources/app.properties"));
        Connection connection;
        String url = prop.getProperty("url");
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String driver = prop.getProperty("driver-class-name");
        Class.forName(driver);
        connection = DriverManager.getConnection(url, username, password);
        return connection; */
}