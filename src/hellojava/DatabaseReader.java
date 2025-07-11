package hellojava;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseReader {
    public static void main(String[] args) {
        Properties props = new Properties();

        try {
            if (args.length < 1) {
                System.out.println("Please enter a .properties file name.");
                return;
            }

            InputStream input = DatabaseReader.class.getClassLoader().getResourceAsStream(args[0]);
            if (input == null) {
                System.out.println(args[0] + " file does not exist.");
                return;
            }

            props.load(input);

            // Hikari config
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("jdbcUrl"));
            config.setUsername(props.getProperty("username"));
            config.setPassword(props.getProperty("password"));
            config.setDriverClassName(props.getProperty("driverClassName"));

            HikariDataSource dataSource = new HikariDataSource(config);

            Connection conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM students");

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Age: " + rs.getInt("age"));
            }

            rs.close();
            stmt.close();
            conn.close();
            dataSource.close();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
} 