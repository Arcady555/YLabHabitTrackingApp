package ru.parfenov.homework_2.server.utility;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.sql.SQLException;

public class LiquibaseUpdate {
    public void run() throws SQLException, IOException, ClassNotFoundException, LiquibaseException {
        java.sql.Connection connection = Utility.loadConnection();

        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

        Liquibase liquibase = new liquibase.Liquibase("db/changelog.xml", new ClassLoaderResourceAccessor(), database);

        liquibase.update(new Contexts(), new LabelExpression());
    }
}