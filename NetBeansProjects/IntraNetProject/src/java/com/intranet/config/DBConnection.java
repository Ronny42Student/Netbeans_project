package com.intranet.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // URL pointant vers le conteneur Docker local sur le port 1521
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/FREEPDB1";

    private static final String USER = "system"; 
    
    // Le mot de passe configuré au lancement de Docker
    private static final String PASS = "SecretPassword123";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Chargement du driver Oracle au lieu de PostgreSQL
        Class.forName("oracle.jdbc.OracleDriver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
