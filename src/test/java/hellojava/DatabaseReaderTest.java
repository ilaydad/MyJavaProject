package hellojava;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariDataSource;

public class DatabaseReaderTest {

    @Test
    void testCreateDataSourceWithValidProperties() {
        Properties props = new Properties();
        props.setProperty("jdbcUrl", "jdbc:mysql://localhost:3306/stajdb");
        props.setProperty("username", "root");
        props.setProperty("password", ""); 
        props.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");

        HikariDataSource ds = DatabaseReader.createDataSource(props);

        assertNotNull(ds, "DataSource should not be null with valid properties.");
        assertEquals("jdbc:mysql://localhost:3306/stajdb", ds.getJdbcUrl());

        ds.close();
    }

    @Test
    void testCreateDataSourceWithMissingProps() {
        Properties props = new Properties(); 
        HikariDataSource ds = DatabaseReader.createDataSource(props);

        assertNull(ds, "DataSource should be null when required properties are missing.");
    }
}
