package com.intranet.dao;

import com.intranet.config.DBConnection;
import com.intranet.model.Logtime;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogtimeDAO {
    // Récupérer les logtimes d'un utilisateur pour un mois donné (par défaut mois courant)
    public List<Logtime> getLogtimesByUser(int userId, int month, int year) {
        List<Logtime> list = new ArrayList<>();
        String sql = "SELECT id, user_id, log_date, hours, description FROM user_logtime WHERE user_id = ? AND EXTRACT(MONTH FROM log_date) = ? AND EXTRACT(YEAR FROM log_date) = ? ORDER BY log_date";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, month);
            ps.setInt(3, year);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Logtime lt = new Logtime();
                    lt.setId(rs.getInt("id"));
                    lt.setUserId(rs.getInt("user_id"));
                    lt.setLogDate(rs.getDate("log_date"));
                    lt.setHours(rs.getDouble("hours"));
                    lt.setDescription(rs.getString("description"));
                    list.add(lt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Ajouter une entrée de logtime
    public boolean addLogtime(Logtime logtime) {
        String sql = "INSERT INTO user_logtime (user_id, log_date, hours, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, logtime.getUserId());
            ps.setDate(2, new java.sql.Date(logtime.getLogDate().getTime()));
            ps.setDouble(3, logtime.getHours());
            ps.setString(4, logtime.getDescription());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Calculer le total des heures pour un utilisateur sur une période (ex: mois en cours)
    public double getTotalHoursForMonth(int userId, int month, int year) {
        String sql = "SELECT SUM(hours) FROM user_logtime WHERE user_id = ? AND EXTRACT(MONTH FROM log_date) = ? AND EXTRACT(YEAR FROM log_date) = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, month);
            ps.setInt(3, year);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}