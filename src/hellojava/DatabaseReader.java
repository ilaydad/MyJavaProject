package hellojava;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.io.InputStream;
import java.io.IOException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseReader {

    private static final Logger logger = Logger.getLogger(DatabaseReader.class.getName());

    public static void main(String[] args) {
        try {
            setupLogger();
        } catch (IOException e) {
            System.out.println("Logger setup failed: " + e.getMessage());
        }

        if (args.length < 1) {
            logger.warning("Properties file not specified.");
            System.out.println("Please enter a .properties file name.");
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

        dataSource.close();
        logger.info("DataSource closed successfully.");
    }

    // sets up the logger to write to a file
    private static void setupLogger() throws IOException {
        FileHandler fileHandler = new FileHandler("application.log", true);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
        logger.setUseParentHandlers(false); // log only to file, not console
    }

    // loads database settings from a properties file
    private static Properties loadProperties(String fileName) {
        Properties props = new Properties();
        try (InputStream input = DatabaseReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                logger.severe(fileName + " file not found.");
                return null;
            }
            props.load(input);
            logger.info("Properties file loaded successfully.");
            return props;
        } catch (IOException e) {
            logger.severe("Error loading properties: " + e.getMessage());
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
            logger.severe("Failed to create connection pool: " + e.getMessage());
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
            logger.severe("SQL error in readStudents(): " + e.getMessage());
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
                logger.warning("No rows affected, student not inserted.");
            }

        } catch (SQLException e) {
            logger.severe("SQL error in insertStudent(): " + e.getMessage());
        }
    }
}
