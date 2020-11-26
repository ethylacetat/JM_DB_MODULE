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

    private static boolean isJDBCConfigured = false;

    public static boolean isJDBCConfigured() {
        return isJDBCConfigured;
    }

    public static void JDBCConfigure() {
        isJDBCConfigured = true;

        Properties dbProperties = new Properties();

        // TODO: Это можно и нужно читать из файла с пропертями
        // ================================
        dbProperties.put("db.driver", "com.mysql.cj.jdbc.Driver");
        dbProperties.put("db.host",
                "jdbc:mysql://localhost:3306/course_3_1_1_3" +
                        "?useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        dbProperties.put("db.login", "application");
        dbProperties.put("db.password", "root");
        // ================================

        JDBCUtil.setProperties(dbProperties);

        String host = dbProperties.getProperty("db.driver");
        try {
            Class.forName(host);
        } catch (ClassNotFoundException e) {
            // TODO: Логировать имя класса драйвера
            logger.log(Level.WARNING, "Unable to connect driver", e);
        }
    }


}
