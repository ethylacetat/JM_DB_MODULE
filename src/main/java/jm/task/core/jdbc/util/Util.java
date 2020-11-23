package jm.task.core.jdbc.util;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    private Util() {
        // NO-OP
    }

    private static final Logger logger = Logger.getLogger(Util.class.getName());

    public static void DBDriverSetup(Properties dbProperties) {
        String host = dbProperties.getProperty("db.driver");
        try {
            Class.forName(host);
            //DriverManager.
        } catch (ClassNotFoundException e) {
            // TODO: Логировать хост
            logger.log(Level.WARNING, "Unable to connect driver", e);
        }
    }

    public static Connection getConnection(Properties dbProperties) throws SQLException {
        String host = dbProperties.getProperty("db.host");
        String login = dbProperties.getProperty("db.login");
        String password = dbProperties.getProperty("db.password");

        return DriverManager.getConnection(host, login, password);
    }

    public static void execute(String query, Properties dbProperties, String... queryArgs) throws SQLException {
        try (Connection connection = getConnection(dbProperties);
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (int index = 1; index <= queryArgs.length; index++) {
                statement.setString(index, queryArgs[index - 1]);
            }
            statement.execute();

        } catch (SQLException e) {
            // TODO: Логировать параметры запроса
            logger.log(Level.WARNING, "Something wrong with query", e);
            throw e;
        }
    }

    public static void execute(String query, Properties dbProperties) throws SQLException {
        try (Connection connection = getConnection(dbProperties);
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            // TODO: Логировать параметры запроса
            logger.log(Level.WARNING, "Something wrong with query", e);
            throw e;
        }
    }

    public static <R> R query(String query, Properties dbProperties,
                              SQLResultConverter<R> converter) throws SQLException {
        try (Connection connection = getConnection(dbProperties);
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(query);
            return handleResult(resultSet, converter);

        } catch (SQLException e) {
            // TODO: Логировать параметры запроса
            logger.log(Level.WARNING, "Something wrong with query", e);
            throw e;
        }
    }

    public static <R> R query(String query, Properties dbProperties,
                              SQLResultConverter<R> converter, String... queryArgs) throws SQLException {

        try (Connection connection = getConnection(dbProperties);
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (int index = 1; index <= queryArgs.length; index++) {
                statement.setString(index, queryArgs[index - 1]);
            }

            ResultSet resultSet = statement.executeQuery(query);
            return handleResult(resultSet, converter);

        } catch (SQLException e) {
            // TODO: Логировать параметры запроса
            logger.log(Level.WARNING, "Something wrong with query", e);
            throw e;
        }
    }

    public static <R> R handleResult(ResultSet statement, SQLResultConverter<R> converter) throws SQLException {
        return converter.apply(statement);
    }

}
