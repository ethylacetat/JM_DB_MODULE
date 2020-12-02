package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.JDBCUtil;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    private final Logger logger = Logger.getLogger(UserDaoJDBCImpl.class.getName());

    public UserDaoJDBCImpl() {
        Util.JDBCConfigure();
    }

    public void createUsersTable() {
        String query = "CREATE TABLE users (\n" +
                "\tid BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "    user_name VARCHAR(255) NOT NULL,\n" +
                "    user_lastname VARCHAR(255) NOT NULL,\n" +
                "    age TINYINT NOT NULL,\n" +
                "    PRIMARY KEY(id)\n" +
                ");";

        try {
            JDBCUtil.execute(query);
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "createUsersTable is failed", sqlException);
        }
    }

    public void dropUsersTable() {
        String query = "DROP TABLE users;";

        try {
            JDBCUtil.execute(query);
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "dropUsersTable is failed", sqlException);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String query = "INSERT INTO users (user_name, user_lastname, age)\n" +
                "VALUES (?, ?, ?);";

        try {
            JDBCUtil.execute(query, name, lastName, Byte.toString(age));
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "saveUser is failed", sqlException);
        }
    }

    public void removeUserById(long id) {
        String query = "DELETE FROM users\n" +
                "WHERE id = ?;";

        try {
            JDBCUtil.execute(query, Long.toString(id));
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "removeUserById is failed", sqlException);
        }
    }

    public List<User> getAllUsers() {
        String query = "SELECT * FROM users;";

        try {
            return JDBCUtil.query(query, result -> {
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

    public void cleanUsersTable() {
        String query = "TRUNCATE users;";

        try {
            JDBCUtil.execute(query);
        } catch (SQLException sqlException) {
            logger.log(Level.WARNING, "cleanUsersTable is failed", sqlException);
        }
    }
}
