package com.recordcataloguer.recordcataloguer.helpers;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class DatabaseHelper {

    public static Connection createConnection(String username, String password) throws SQLException {
        // TODO: Add validation
        return establishDBConnection(username, password);
    }

    private static void closeDBConnection(Connection connection) throws SQLException {
        connection.close();
        log.info("Connection to DB closed for DB connection {}", connection.getClientInfo());
    }
    private static Connection establishDBConnection(String username, String password) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        props.setProperty("ssl", "true");
        Connection connection = DriverManager.getConnection(url, props);

        return connection;
    }

}
