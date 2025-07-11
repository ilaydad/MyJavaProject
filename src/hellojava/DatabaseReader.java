package hellojava;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseReader {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/stajdb";
        String user = "root";
        String password = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            String sql = "SELECT * FROM students";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");

                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
} 