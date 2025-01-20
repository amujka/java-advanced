package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
    public static Connection createConnection(){
    int PORT = 1433;
    String URL = String.format("jdbc:sqlserver://localhost\\\\SQLEXPRESS:%d;databaseName=AdventureWorksOBP;encrypt=true;trustServerCertificate=true", PORT);
    String user = "root";
    String password = "password";
         try{
             Connection connection = DriverManager.getConnection(URL,user,password);
             if (connection != null) System.out.printf("Connection successful on port: %d%n", PORT);
             return connection;
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return null;
    }
}
