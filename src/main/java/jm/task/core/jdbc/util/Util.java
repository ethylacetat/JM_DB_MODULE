package jm.task.core.jdbc.util;

import java.io.File;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Util {

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
            // TODO: Логировать имя класса драйвера
            logger.log(Level.WARNING, "Unable to connect driver", e);
        }
    }

    private static Properties dbProperties;

    public static void dbSetup() {
        dbProperties = new Properties();

        // TODO: Это можно и нужно читать из файла с пропертями
        // ================================
        dbProperties.put("db.driver", "com.mysql.cj.jdbc.Driver");
        dbProperties.put("db.host",
                "jdbc:mysql://localhost:3306/course_3_1_1_3" +
                        "?useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        dbProperties.put("db.login", "application");
        dbProperties.put("db.password", "root");
        // ================================

        String host = dbProperties.getProperty("db.driver");
        try {
            Class.forName(host);
        } catch (ClassNotFoundException e) {
            // TODO: Логировать имя класса драйвера
            logger.log(Level.WARNING, "Unable to connect driver", e);
        }

        try {
            retrieveConnection();
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Unable to create connection", e);
        }
    }

    private static Connection openedConnection = null;


    private static void retrieveConnection() throws SQLException {
        // isValid(ms) скольо ждать нужно?
        if (openedConnection == null || !openedConnection.isValid(10)) {

            if (openedConnection != null) {
                try {
                    openedConnection.close();
                } catch (SQLException e) {
                    // IGNORE
                }
            }

            String host = dbProperties.getProperty("db.host");
            String login = dbProperties.getProperty("db.login");
            String password = dbProperties.getProperty("db.password");

            openedConnection = DriverManager.getConnection(host, login, password);

        } else {
            logger.warning("Connection already established");
        }
    }

    public static Connection getConnection() throws SQLException {
        if (openedConnection == null) {
            retrieveConnection();
        }
        return ConnectionWrapper.wrap(openedConnection);
    }

    public static void execute(String query, String... queryArgs) throws SQLException {
        try (Connection connection = getConnection();
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

    public static void execute(String query) throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            // TODO: Логировать параметры запроса
            logger.log(Level.WARNING, "Something wrong with query", e);
            throw e;
        }
    }

    public static <R> R query(String query, SQLResultConverter<R> converter) throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            return handleResult(resultSet, converter);
        } catch (SQLException e) {
            // TODO: Логировать параметры запроса
            logger.log(Level.WARNING, "Something wrong with query", e);
            throw e;
        }
    }

    public static <R> R query(String query, SQLResultConverter<R> converter, String... queryArgs) throws SQLException {
        try (Connection connection = getConnection();
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
