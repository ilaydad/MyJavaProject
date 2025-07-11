package hellojava;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseReader {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("please enter a .properties file name.");
            return;
        }

        Properties props = loadProperties(args[0]);
        if (props == null) return;

        HikariDataSource dataSource = createDataSource(props);
        if (dataSource == null) return;

        readStudents(dataSource);

        dataSource.close(); 
    }

    // loads database settings from .properties file
    private static Properties loadProperties(String fileName) {
        Properties props = new Properties();
        try (InputStream input = DatabaseReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.out.println(fileName + " file does not exist.");
                return null;
            }
            props.load(input);
            return props;
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
            return null;
        }
    }

    // creates database connection pool using HikariCP
    private static HikariDataSource createDataSource(Properties props) {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("jdbcUrl"));
            config.setUsername(props.getProperty("username"));
            config.setPassword(props.getProperty("password"));
            config.setDriverClassName(props.getProperty("driverClassName"));
            return new HikariDataSource(config);
        } catch (Exception e) {
            System.out.println("Failed to create DataSource: " + e.getMessage());
            return null;
        }
    }

    // it retrieves the data from the students table and prints it to the screen.
    private static void readStudents(HikariDataSource dataSource) {
        String query = "SELECT * FROM students";

        // using try-with-resources automatically closes Connection, Statement and ResultSet
        try (
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)
        ) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Age: " + rs.getInt("age"));
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }
}
