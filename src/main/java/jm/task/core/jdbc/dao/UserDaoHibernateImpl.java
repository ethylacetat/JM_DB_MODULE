package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.logging.Logger;

public class UserDaoHibernateImpl implements UserDao {

    private static Logger logger = Logger.getLogger(UserDaoHibernateImpl.class.getName());

    public UserDaoHibernateImpl() {
        Util.hibernateConfigure();
    }


    private static final String CREATE_USER_TABLE_QUERY =
            "CREATE TABLE IF NOT EXISTS users (\n" +
                    "\tid BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "    name VARCHAR(255) NOT NULL,\n" +
                    "    lastname VARCHAR(255) NOT NULL,\n" +
                    "    age TINYINT NOT NULL,\n" +
                    "    PRIMARY KEY(id)\n" +
                    ");";

    @Override
    public void createUsersTable() {
        HibernateUtil.nativeExecute(CREATE_USER_TABLE_QUERY);
    }

    private static final String DROP_USER_TABLE_QUERY = "DROP TABLE IF EXISTS users;";

    @Override
    public void dropUsersTable() {
        HibernateUtil.nativeExecute(DROP_USER_TABLE_QUERY);
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

    private static final String CLEAR_USER_TABLE_QUERY = "TRUNCATE users;";

    @Override
    public void cleanUsersTable() {
        HibernateUtil.nativeExecute(CLEAR_USER_TABLE_QUERY);
    }
}
