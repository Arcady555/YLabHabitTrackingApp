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
}