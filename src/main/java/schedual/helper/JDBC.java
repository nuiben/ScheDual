package schedual.helper;

import javafx.scene.control.Alert;


import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Java Database Connectivity API Class
 */

public class JDBC {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference

    private static Connection connection;  // Connection Interface
    private static String user;

    /**
     * Establishes a connection to the MySQL Database "databaseName"
     * @param userName the user credential provided to MySQL during a login attempt.
     * @param password the password provided to MySQL during a login attempt.
     * @return Boolean indicating the success or failure of the connection attempt.
     */
    public static Boolean openConnection(String userName, String password) {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            user = userName;
            return true;
        }
        catch(ClassNotFoundException | SQLException e) {
            Alert errorMessage = new Alert(Alert.AlertType.ERROR);
            errorMessage.setTitle(ResourceBundle.getBundle("schedual/bundle/lang", Locale.getDefault()).getString("LogInError"));
            errorMessage.setContentText(ResourceBundle.getBundle("schedual/bundle/lang", Locale.getDefault()).getString("LogInErrorMessage"));
            errorMessage.show();
            return false;
        }
    }

    /**
     * Returns the current open connection.
     * @return current connection.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Returns the userName used to establish the currently open connection.
     * @return user
     */
    public static String getUserName() {
        return user;
    }

    /**
     * Closes the connection to MySQL database.
     */
    public static void closeConnection() {
        try {
            connection.close();
        }
        catch(SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
