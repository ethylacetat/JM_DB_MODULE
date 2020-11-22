package jm.task.core.jdbc.util;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLResultConverter<R> {

    R apply(ResultSet resultSet) throws SQLException;
}
