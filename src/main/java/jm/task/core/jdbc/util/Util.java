package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

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

    // JDBC конфигурация
    // ================================
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
    // ================================

    // Hibernate конфигурация
    // ================================
    private static boolean isHibernateConfigured = false;

    public static boolean isHibernateConfigured() {
        return isHibernateConfigured;
    }

    private static ServiceRegistry registry = null;

    public static void hibernateConfigure() {

        // Предотвращает повторную конфигурацию, из-за чего локи на таблицах и повисшие коннекты
        if(isHibernateConfigured) {
            return;
        }

        isHibernateConfigured = true;

        Properties configurationProperties = new Properties();
        configurationProperties.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        configurationProperties.put(Environment.URL, "jdbc:mysql://localhost:3306/course_3_1_1_3" +
                "?useUnicode=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        configurationProperties.put(Environment.USER, "application");
        configurationProperties.put(Environment.PASS, "root");
        configurationProperties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        configurationProperties.put(Environment.SHOW_SQL, "true");
        Configuration configuration = new Configuration();

        configuration.setProperties(configurationProperties);
        //configuration.addPackage("jm.task.core.jdbc.model");
        configuration.addAnnotatedClass(User.class);


        registry = new StandardServiceRegistryBuilder().
                applySettings(configuration.getProperties()).build();


        HibernateUtil.setFactory(configuration.buildSessionFactory(registry));
    }

    public static void hibernateDestroy() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
    // ================================

}
