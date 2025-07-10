package hellojava;
<<<<<<< HEAD
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

         
=======

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseReader {
    public static void main(String[] args) {
        Properties props = new Properties();

        try {
            InputStream input = DatabaseReader.class.getClassLoader().getResourceAsStream("db.properties");
            if (input == null) {
                System.out.println("Could not find db.properties file.");
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
                System.out.println("ID: " + rs.getInt("id") +
                                   ", Name: " + rs.getString("name") +
                                   ", Age: " + rs.getInt("age"));
            }

>>>>>>> pojo
            rs.close();
            stmt.close();
            conn.close();

<<<<<<< HEAD
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
=======
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
>>>>>>> pojo
        }
    }
}

<<<<<<< HEAD
=======

>>>>>>> pojo
