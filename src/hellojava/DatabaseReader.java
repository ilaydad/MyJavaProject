package hellojava;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseReader {

    private static final Logger logger = LogManager.getLogger(DatabaseReader.class);

    public static void main(String[] args) {
     if (args.length < 1) {
            logger.warn("properties file not specified.");
            System.out.println("please enter a .properties file name.");
            return;
        }

        Properties props = loadProperties(args[0]);
        if (props == null) return;

        HikariDataSource dataSource = createDataSource(props);
        if (dataSource == null) return;

        // inserting a new student record
        insertStudent(dataSource, "Ceyda", 21);
       
        // reading and displaying all students
        readStudents(dataSource);
        for (int i = 0; i < 500; i++) {
        	logger.error("Test log entry number " + i + " — something went wrong, or maybe not!");
}

        dataSource.close();
        logger.info("DataSource closed successfully.");
    }
    
    private static Properties loadProperties(String fileName) {
        Properties props = new Properties();
        try (InputStream input = DatabaseReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                logger.error(fileName + " file not found.");
                return null;
            }
            props.load(input);
            logger.info("Properties file loaded successfully.");
            return props;
        } catch (IOException e) {
            logger.error("Error loading properties: ", e);
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

            logger.info("HikariCP connection pool created.");
            return new HikariDataSource(config);
        } catch (Exception e) {
            logger.error("Failed to create connection pool: " + e.getMessage());
            return null;
        }
    }

    // reads and prints all students from the database
    private static void readStudents(HikariDataSource dataSource) {
        String query = "SELECT * FROM students";
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
            logger.info("Student records read successfully.");
        } catch (SQLException e) {
            logger.error("SQL error in readStudents(): " + e.getMessage());
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
                logger.info("New student inserted: " + name);
            } else {
                logger.warn("No rows affected, student not inserted.");
            }

        } catch (SQLException e) {
            logger.error("SQL error in insertStudent(): " + e.getMessage());
        }
    }
}
