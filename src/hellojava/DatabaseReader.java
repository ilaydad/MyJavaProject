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

        // Sadece bir kez eklenmemişse ekle
        insertStudentIfNotExists(dataSource, "İlayda", 22);

        // Kayıtları oku ve yazdır
        readStudents(dataSource);

        dataSource.close();
        logger.info("DataSource closed successfully.");
    }

    public static Properties loadProperties(String fileName) {
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

    public static HikariDataSource createDataSource(Properties props) {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("jdbcUrl"));
            config.setUsername(props.getProperty("username"));
            config.setPassword(props.getProperty("password", "")); // boş şifre desteği
            config.setDriverClassName(props.getProperty("driverClassName"));
            logger.info("HikariCP connection pool created.");
            return new HikariDataSource(config);
        } catch (Exception e) {
            logger.error("Failed to create connection pool: " + e.getMessage());
            return null;
        }
    }

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

    private static void insertStudentIfNotExists(HikariDataSource dataSource, String name, int age) {
        String checkQuery = "SELECT COUNT(*) FROM students WHERE name = ? AND age = ?";
        String insertQuery = "INSERT INTO students (name, age) VALUES (?, ?)";

        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery)
        ) {
            checkStmt.setString(1, name);
            checkStmt.setInt(2, age);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                insertStmt.setString(1, name);
                insertStmt.setInt(2, age);
                int rows = insertStmt.executeUpdate();
                if (rows > 0) {
                    logger.info("Student inserted: " + name);
                }
            } else {
                logger.info("Student already exists: " + name);
            }

        } catch (SQLException e) {
            logger.error("SQL error in insertStudentIfNotExists(): " + e.getMessage());
        }
    }
}
