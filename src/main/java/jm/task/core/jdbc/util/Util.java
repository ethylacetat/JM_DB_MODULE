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
        try {
            Class.forName(dbProperties.getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            // TODO: Логировать хост
            logger.log(Level.WARNING, "Unable to connect driver", e);
        }


    }

    public static Connection getConnection(Properties dbProperties) {
        Connection connection = null;

        String host = dbProperties.getProperty("db.host");
        String login = dbProperties.getProperty("db.login");
        String password = dbProperties.getProperty("db.password");

        try {
            connection = DriverManager.getConnection(host, login, password);
        } catch (SQLException e) {
            logger.log(Level.WARNING,
                    "database address: {}; database login: {}", new Object[]{host, login});
            logger.log(Level.WARNING, "Unable to create sqlConnections", e);
        }

        return connection;
    }

    public static <R> R query(String query, Properties dbProperties, SQLResultConverter<R> converter) {

        Connection connection = getConnection(dbProperties);

        if (connection != null) {
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                return converter.apply(resultSet);
            } catch (SQLException e) {
                // TODO: Логировать проблемный запрос
                logger.log(Level.WARNING, "Something wrong with query", e);
            }
        }

        return null;
    }

}
