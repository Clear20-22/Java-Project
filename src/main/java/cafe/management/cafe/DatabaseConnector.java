package cafe.management.cafe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private Connection databaseLink;

    public Connection getConnection() {
        String databaseName = "your_database_name"; // Replace with your database name
        String databaseUser = "your_username";     // Replace with your database username
        String databasePassword = "your_password"; // Replace with your database password
        String url = "jdbc:mysql://localhost:3306/" + databaseName; // Adjust host and port if needed

        try {
            // Load the MySQL JDBC driver (optional for newer versions)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the database
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL Driver not found. Please check your library dependencies.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection to the database failed!");
            e.printStackTrace();
        }

        return databaseLink;
    }
}
