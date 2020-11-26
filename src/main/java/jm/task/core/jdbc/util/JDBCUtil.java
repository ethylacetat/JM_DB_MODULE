package jm.task.core.jdbc.util;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCUtil {

    private static final Logger logger = Logger.getLogger(JDBCUtil.class.getName());
    private static Properties dbProperties;
    private static Connection openedConnection = null;

    private JDBCUtil() {
        // NO-OP
    }

    public static void setProperties(Properties properties) {
        JDBCUtil.dbProperties = properties;
    }

    private static void checkedConfigured() throws SQLException {
        if (!Util.isJDBCConfigured()) {
            throw new SQLException("JDBC is not configured");
        }
    }

    static void retrieveConnection() throws SQLException {
        checkedConfigured();
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
        checkedConfigured();
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
