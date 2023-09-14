package dev.danilosantos.infrastructure.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
    private final String URL = "jdbc:postgresql://localhost:5432/atividadenewgo";
    private final String USER = "postgres";
    private final String PASSWORD = "postgres";

    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
