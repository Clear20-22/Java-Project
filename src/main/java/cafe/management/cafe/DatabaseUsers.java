package cafe.management.cafe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUsers {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/cafemanagement";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "2022315898";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");


            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
//            System.out.println("Connected to the database successfully!");

        } catch (ClassNotFoundException e) {
//            System.err.println("MySQL JDBC Driver not found. Add the library to your project.");
            e.printStackTrace();
        } catch (SQLException e) {
//            System.err.println("Failed to connect to the database!");
            e.printStackTrace();
        }

        return connection;
    }
}
