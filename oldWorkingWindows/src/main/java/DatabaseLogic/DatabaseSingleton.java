package DatabaseLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseSingleton {
    private static DatabaseSingleton ref;

    private Connection connection;

    private DatabaseSingleton() {
		try {

            //Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }

    public static DatabaseSingleton getDatabaseSingleton() {
        if (ref == null)
            ref = new DatabaseSingleton();
        return ref;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();

    }

    public Connection getConnection(boolean autoCommit) throws SQLException {
        if (connection == null || connection.isClosed()) {

            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://"+DatabaseProperties.HOST,
                        DatabaseProperties.USERNAME,
                        DatabaseProperties.PASSWORD);

            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }

        //connection.setAutoCommit(autoCommit);

        return connection;
    }

}
