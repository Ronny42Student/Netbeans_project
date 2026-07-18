package com.intranet.dao;

import com.intranet.config.DBConnection;
import com.intranet.model.Progress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgressDAO {

    public List<Progress> getProgressByUser(int userId) {
        List<Progress> list = new ArrayList<>();
        String sql = "SELECT up.user_id, up.project_id, up.status, up.grade, up.attempt_number, " +
                     "up.penalty_percent, up.enrolled_at, up.deadline, up.failed_at, p.name AS project_name " +
                     "FROM user_progress up JOIN projects p ON p.id = up.project_id " +
                     "WHERE up.user_id = ? ORDER BY up.enrolled_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Progress> getAllProgress() {
        List<Progress> list = new ArrayList<>();
        String sql = "SELECT up.user_id, up.project_id, up.status, up.grade, up.attempt_number, " +
                     "up.penalty_percent, up.enrolled_at, up.deadline, up.failed_at, p.name AS project_name, " +
                     "u.username FROM user_progress up " +
                     "JOIN projects p ON p.id = up.project_id " +
                     "JOIN users u ON u.id = up.user_id " +
                     "ORDER BY up.enrolled_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Progress p = mapRow(rs);
                p.setUsername(rs.getString("username"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean enrollUser(int userId, int projectId, int deadlineDays) {
        String checkSql = "SELECT 1 FROM user_progress WHERE user_id = ? AND project_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
            checkPs.setInt(1, userId);
            checkPs.setInt(2, projectId);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next()) return false; // déjà inscrit
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String sql = "INSERT INTO user_progress (user_id, project_id, status, attempt_number, " +
                     "penalty_percent, enrolled_at, deadline) VALUES (?, ?, 'IN_PROGRESS', 1, 0, " +
                     "CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, projectId);
            ps.setInt(3, deadlineDays);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean markFailed(int userId, int projectId, double penaltyPercent) {
        String sql = "UPDATE user_progress SET status = 'FAILED', penalty_percent = ?, " +
                     "failed_at = CURRENT_TIMESTAMP WHERE user_id = ? AND project_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, penaltyPercent);
            ps.setInt(2, userId);
            ps.setInt(3, projectId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean retryProject(int userId, int projectId, int deadlineDays) {
        String sql = "UPDATE user_progress SET status = 'IN_PROGRESS', attempt_number = attempt_number + 1, " +
                     "deadline = CURRENT_TIMESTAMP + ?, grade = NULL, failed_at = NULL " +
                     "WHERE user_id = ? AND project_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deadlineDays);
            ps.setInt(2, userId);
            ps.setInt(3, projectId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean completeProject(int userId, int projectId, int grade) {
        String sql = "UPDATE user_progress SET status = 'COMPLETED', grade = ? WHERE user_id = ? AND project_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, grade);
            ps.setInt(2, userId);
            ps.setInt(3, projectId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeEnrollment(int userId, int projectId) {
        String sql = "DELETE FROM user_progress WHERE user_id = ? AND project_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, projectId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getCompletedCountForUser(int userId) {
        String sql = "SELECT COUNT(*) FROM user_progress WHERE user_id = ? AND status = 'COMPLETED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Progress mapRow(ResultSet rs) throws SQLException {
        Progress p = new Progress();
        p.setUserId(rs.getInt("user_id"));
        p.setProjectId(rs.getInt("project_id"));
        p.setStatus(rs.getString("status"));
        int grade = rs.getInt("grade");
        p.setGrade(rs.wasNull() ? null : grade);
        p.setAttemptNumber(rs.getInt("attempt_number"));
        p.setPenaltyPercent(rs.getDouble("penalty_percent"));
        p.setEnrolledAt(rs.getTimestamp("enrolled_at"));
        p.setDeadline(rs.getTimestamp("deadline"));
        p.setFailedAt(rs.getTimestamp("failed_at"));
        p.setProjectName(rs.getString("project_name"));
        return p;
    }
}