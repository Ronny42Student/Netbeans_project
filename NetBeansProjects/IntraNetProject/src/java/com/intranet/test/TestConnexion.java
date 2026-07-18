package com.intranet.test;

import com.intranet.config.DBConnection;
import java.sql.Connection;

public class TestConnexion {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn != null) {
                System.out.println("CONNEXION RÉUSSIE À ORACLE !");
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ÉCHEC DE LA CONNEXION : " + e.getMessage());
        }
    }
}