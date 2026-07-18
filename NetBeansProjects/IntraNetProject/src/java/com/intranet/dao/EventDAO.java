package com.intranet.dao;

import com.intranet.config.DBConnection;
import com.intranet.model.Event;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    public List<Event> getUpcomingEvents(int limit) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT id, title, event_type, event_date, location FROM events WHERE event_date >= CURRENT_TIMESTAMP ORDER BY event_date FETCH FIRST ? ROWS ONLY";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Event e = new Event();
                    e.setId(rs.getInt("id"));
                    e.setTitle(rs.getString("title"));
                    e.setEventType(rs.getString("event_type"));
                    e.setEventDate(rs.getTimestamp("event_date"));
                    e.setLocation(rs.getString("location"));
                    events.add(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return events;
    }

    public boolean registerToEvent(int userId, int eventId) {
        String sql = "INSERT INTO event_registrations (user_id, event_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean unregisterFromEvent(int userId, int eventId) {
        String sql = "DELETE FROM event_registrations WHERE user_id = ? AND event_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isRegistered(int userId, int eventId) {
        String sql = "SELECT 1 FROM event_registrations WHERE user_id = ? AND event_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Integer> getRegisteredEventIds(int userId) {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT event_id FROM event_registrations WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt("event_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }
}