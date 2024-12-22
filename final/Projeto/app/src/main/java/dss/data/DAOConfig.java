package dss.data;

public class DAOConfig {
    static final String USERNAME = "me";
    static final String PASSWORD = "mypass";                    
    private static final String DATABASE = "dss";
    private static final String DRIVER = "jdbc:mariadb";
    static final String URL = DRIVER+"://localhost:3306/"+DATABASE;
}
