package dss.data;

import java.sql.*;

public class DAOConfig {
    static final String USERNAME = "me";
    static final String PASSWORD = "mypass";                    
    private static final String DATABASE = "dss";
    private static final String DRIVER = "jdbc:mariadb";
    static final String URL = DRIVER+"://localhost:3306/"+DATABASE;
    static final Connection connection = connect();

    private static Connection connect() {
        try {
            System.out.println("Connecting to database...");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection failed!" + e.getMessage());
            System.exit(1);
            return null;
        }
    }
}
