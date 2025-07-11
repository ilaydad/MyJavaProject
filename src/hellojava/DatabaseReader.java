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

        // inserting a new student record
        insertStudent(dataSource, "Zehra", 20);

        // reading and displaying all students
        readStudents(dataSource);

        dataSource.close(); // closing the datasource
    }

    // loads database settings from a properties file
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

    // creates a HikariCP connection pool
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

    // reads all students from the database and prints them
    private static void readStudents(HikariDataSource dataSource) {
        try (
            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students")
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

    // inserts a new student record into the database
    private static void insertStudent(HikariDataSource dataSource, String name, int age) {
        String sql = "INSERT INTO students (name, age) VALUES (?, ?)";

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("student inserted successfully.");
            } else {
                System.out.println("failed to insert student.");
            }

        } catch (SQLException e) {
            System.out.println("SQL error during insert: " + e.getMessage());
        }
    }
}
