package hellojava;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

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

            String url = props.getProperty("url");
            String user = props.getProperty("user");
            String password = props.getProperty("password");

            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            String sql = "SELECT * FROM students";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id")
                        + ", Name: " + rs.getString("name")
                        + ", Age: " + rs.getInt("age"));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }
}
