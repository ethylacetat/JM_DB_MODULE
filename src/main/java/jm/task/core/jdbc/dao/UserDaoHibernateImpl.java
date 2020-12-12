package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;
import jm.task.core.jdbc.util.Util;

import java.util.List;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private static Logger logger = Logger.getLogger(UserDaoHibernateImpl.class.getName());

    public UserDaoHibernateImpl() {
        Util.hibernateConfigure();
    }

    @Override
    public void createUsersTable() {
        String query = "CREATE TABLE IF NOT EXISTS users (\n" +
                "\tid BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "    name VARCHAR(255) NOT NULL,\n" +
                "    lastname VARCHAR(255) NOT NULL,\n" +
                "    age TINYINT NOT NULL,\n" +
                "    PRIMARY KEY(id)\n" +
                ");";
        HibernateUtil.nativeExecute(query);
    }

    @Override
    public void dropUsersTable() {
        String query = "DROP TABLE IF EXISTS users;";
        HibernateUtil.nativeExecute(query);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        HibernateUtil.execute(session -> {
            User user = new User(name, lastName, age);
            session.save(user);
        });

    }

    @Override
    public void removeUserById(long id) {
        HibernateUtil.execute(session -> {
            User user = new User();
            user.setId(id);
            session.delete(user);
        });
    }

    @Override
    public List<User> getAllUsers() {
        return HibernateUtil.nativeQuery("SELECT * FROM users;", User.class);
    }

    @Override
    public void cleanUsersTable() {
        String query = "TRUNCATE users;";
        HibernateUtil.nativeExecute(query);
    }
}
