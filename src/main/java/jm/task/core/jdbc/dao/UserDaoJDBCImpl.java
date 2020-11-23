package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import javax.print.DocFlavor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    private final Properties dbProperties;

    private final Logger logger = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public UserDaoJDBCImpl() {
        dbProperties = new Properties();

        // TODO: ЧИтаем из файла, и читаем явно не тут
        // ================================
        dbProperties.put("db.driver", "com.mysql.cj.jdbc.Driver");
        dbProperties.put("db.host", "jdbc:mysql://localhost:3306/course_3_1_1_3?useUnicode=true&serverTimezone=UTC");
        dbProperties.put("db.login", "root");
        dbProperties.put("db.password", "root");
        // ================================

        // TODO: Кидать исключение, тк без драйвера приложение работать не может
        Util.DBDriverSetup(dbProperties);
    }

    private static final String CREATE_USER_TABLE_QUERY =
            "CREATE TABLE users (\n" +
                    "\tid BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "    user_name VARCHAR(255) NOT NULL,\n" +
                    "    user_lastname VARCHAR(255) NOT NULL,\n" +
                    "    age TINYINT NOT NULL,\n" +
                    "    PRIMARY KEY(id)\n" +
                    ");";

    public void createUsersTable() {
        try {
            Util.execute(CREATE_USER_TABLE_QUERY, dbProperties);
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "createUsersTable is failed", sqlException);
        }
    }

    private static final String DROP_USER_TABLE_QUERY = "DROP TABLE users;";

    public void dropUsersTable() {
        try {
            Util.execute(DROP_USER_TABLE_QUERY, dbProperties);
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "dropUsersTable is failed", sqlException);
        }
    }

    private static final String INSERT_USER_QUERY =
            "INSERT INTO users (user_name, user_lastname, age)\n" +
                    "VALUES (?, ?, ?);";

    public void saveUser(String name, String lastName, byte age) {
        try {
            Util.execute(INSERT_USER_QUERY, dbProperties, name, lastName, Byte.toString(age));
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "saveUser is failed", sqlException);
        }
    }

    private static final String DELETE_USER_BY_ID_QUERY =
            "DELETE FROM users\n" +
                    "WHERE id = ?;";

    public void removeUserById(long id) {
        try {
            Util.execute(DELETE_USER_BY_ID_QUERY, dbProperties, Long.toString(id));
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "removeUserById is failed", sqlException);
        }
    }

    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users;";

    public List<User> getAllUsers() {
        try {
            return Util.query(GET_ALL_USERS_QUERY, dbProperties, result -> {
                List<User> users = new ArrayList<>();

                while (result.next()) {
                    User user = new User();

                    user.setId(result.getLong(1));
                    user.setName(result.getString(2));
                    user.setLastName(result.getString(3));
                    user.setAge(result.getByte(4));

                    users.add(user);
                }
                return users;
            });
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "removeUserById is failed", sqlException);
        }

        return Collections.emptyList();
    }

    private static final String CLEAR_USER_TABLE_QUERY = "TRUNCATE users;";

    public void cleanUsersTable() {
        try {
            Util.execute(CLEAR_USER_TABLE_QUERY, dbProperties);
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "cleanUsersTable is failed", sqlException);
        }
    }
}
