/*
 * This class is used to establish a connection
 * with the postgreSQL database. It is used in
 * other classes to retrieve the Connection object.
 */

package sqlproject;

import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.File;

public class DatabaseUtil {
    
    Connection connection;
    Statement statement;

    DatabaseUtil() {
        Properties properties = new Properties();
        File file = new File("../src/main/resources/database.properties");
        String path = file.getAbsolutePath();
        try {
            // Loading the properties file.
            InputStream input = new FileInputStream(path);
            properties.load(input);

            // Get database variable values.
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
        
            // Register the PostgreSQL driver.
            Class.forName("org.postgresql.Driver");

            // Establish the database connection.
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC driver not found! " +
                                "Is JAR file present?");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database connection error! " +
                                "Check the src/main/resources/database.resources file.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Database resources file not found! " + 
                                "Check the src/main/resources directory.");
            e.printStackTrace();
        }
    }

    protected Connection getConnection() {
        return connection;
    }

    protected Statement getStatement() {
        return statement;
    }

    protected void closeDatabase() throws SQLException {
        statement.close();
        connection.close();
    }
}