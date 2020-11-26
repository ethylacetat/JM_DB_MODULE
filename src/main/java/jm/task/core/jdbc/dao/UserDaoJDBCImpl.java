package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.JDBCUtil;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    private final Logger logger = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public UserDaoJDBCImpl() {
        Util.JDBCConfigure();
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
            JDBCUtil.execute(CREATE_USER_TABLE_QUERY);
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "createUsersTable is failed", sqlException);
        }
    }

    private static final String DROP_USER_TABLE_QUERY = "DROP TABLE users;";

    public void dropUsersTable() {
        try {
            JDBCUtil.execute(DROP_USER_TABLE_QUERY);
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "dropUsersTable is failed", sqlException);
        }
    }

    private static final String INSERT_USER_QUERY =
            "INSERT INTO users (user_name, user_lastname, age)\n" +
                    "VALUES (?, ?, ?);";

    public void saveUser(String name, String lastName, byte age) {
        try {
            JDBCUtil.execute(INSERT_USER_QUERY, name, lastName, Byte.toString(age));
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "saveUser is failed", sqlException);
        }
    }

    private static final String DELETE_USER_BY_ID_QUERY =
            "DELETE FROM users\n" +
                    "WHERE id = ?;";

    public void removeUserById(long id) {
        try {
            JDBCUtil.execute(DELETE_USER_BY_ID_QUERY, Long.toString(id));
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "removeUserById is failed", sqlException);
        }
    }

    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users;";

    public List<User> getAllUsers() {
        try {
            return JDBCUtil.query(GET_ALL_USERS_QUERY, result -> {
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
            JDBCUtil.execute(CLEAR_USER_TABLE_QUERY);
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "cleanUsersTable is failed", sqlException);
        }
    }
}
