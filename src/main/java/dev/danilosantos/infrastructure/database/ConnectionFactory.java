package dev.danilosantos.infrastructure.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
    private final String url = "jdbc:postgresql://localhost:5432/atividadenewgo";
    private final String user = "postgres";
    private final String password = "postgres";

    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
