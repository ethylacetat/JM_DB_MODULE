package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private static final String LOG_USER_MESSAGE_FORMAT = "User с именем – {0} добавлен в базу данных";

    public static void main(String[] args) {

        String[] names = {"Vasian", "Anatoli", "Anton", "Petr"};
        String[] lastnames = {"Vasian", "Anatoli", "Anton", "Petr"};
        byte[] ages = {1, 12, 63, 122};

        UserService service = new UserServiceImpl();

        service.createUsersTable();

        for (int i = 0; i < 4; i++) {
            service.saveUser(names[i], lastnames[i], ages[i]);
            logger.log(Level.INFO, LOG_USER_MESSAGE_FORMAT, names[i]);
        }

        List<User> users = service.getAllUsers();

        for (User user : users) {
            logger.info(user.toString());
        }

        service.cleanUsersTable();
        service.dropUsersTable();
    }
}
