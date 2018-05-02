package DatabaseLogic;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO {
    private static Connection connection;

    protected static Connection getConnection() {
        return connection;
    }

    protected void setConnection(Connection connection) {
        this.connection = connection;
    }

    public BaseDAO() {
        try {
            setConnection(DatabaseSingleton.getDatabaseSingleton().getConnection(true));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
