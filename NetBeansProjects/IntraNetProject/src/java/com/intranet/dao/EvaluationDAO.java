package com.intranet.dao;

import com.intranet.config.DBConnection;
import com.intranet.model.Evaluation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvaluationDAO {
    public List<Evaluation> getPendingEvaluationsForUser(int userId) {
        List<Evaluation> list = new ArrayList<>();
        String sql = "SELECT id, project_id, user_id, evaluator_id, status, scheduled_date, grade, comments FROM evaluations WHERE user_id = ? AND status = 'PENDING' ORDER BY scheduled_date";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Evaluation e = new Evaluation();
                    e.setId(rs.getInt("id"));
                    e.setProjectId(rs.getInt("project_id"));
                    e.setUserId(rs.getInt("user_id"));
                    e.setEvaluatorId(rs.getInt("evaluator_id"));
                    if (rs.wasNull()) e.setEvaluatorId(null);
                    e.setStatus(rs.getString("status"));
                    e.setScheduledDate(rs.getTimestamp("scheduled_date"));
                    e.setGrade(rs.getInt("grade"));
                    if (rs.wasNull()) e.setGrade(null);
                    e.setComment(rs.getString("comments"));
                    list.add(e);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public boolean addEvaluation(Evaluation eval) {
        String sql = "INSERT INTO evaluations (project_id, user_id, evaluator_id, status, scheduled_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eval.getProjectId());
            ps.setInt(2, eval.getUserId());
            if (eval.getEvaluatorId() != null) {
                ps.setInt(3, eval.getEvaluatorId());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, eval.getStatus());
            ps.setTimestamp(5, new Timestamp(eval.getScheduledDate().getTime()));
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateEvaluationGrade(int evalId, int grade, String comments) {
        String sql = "UPDATE evaluations SET grade = ?, comments = ?, status = 'COMPLETED' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, grade);
            ps.setString(2, comments);
            ps.setInt(3, evalId);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Evaluation> getAllEvaluations() {
        List<Evaluation> list = new ArrayList<>();
        String sql = "SELECT id, project_id, user_id, evaluator_id, status, scheduled_date, grade, " +
                     "comments, final_grade, passed FROM evaluations ORDER BY scheduled_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public boolean submitEvaluationResult(int evalId, int finalGrade, boolean passed, String comments) {
        String sql = "UPDATE evaluations SET final_grade = ?, passed = ?, comments = ?, status = 'COMPLETED' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, finalGrade);
            ps.setInt(2, passed ? 1 : 0);
            ps.setString(3, comments);
            ps.setInt(4, evalId);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Evaluation getEvaluationById(int evalId) {
        String sql = "SELECT id, project_id, user_id, evaluator_id, status, scheduled_date, grade, " +
                     "comments, final_grade, passed FROM evaluations WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, evalId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean deleteEvaluation(int evalId) {
        String sql = "DELETE FROM evaluations WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, evalId);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean rescheduleEvaluation(int evalId, java.util.Date newDate) {
        String sql = "UPDATE evaluations SET scheduled_date = ?, status = 'PENDING' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, new Timestamp(newDate.getTime()));
            ps.setInt(2, evalId);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private Evaluation mapRow(ResultSet rs) throws SQLException {
        Evaluation e = new Evaluation();
        e.setId(rs.getInt("id"));
        e.setProjectId(rs.getInt("project_id"));
        e.setUserId(rs.getInt("user_id"));
        int evaluatorId = rs.getInt("evaluator_id");
        e.setEvaluatorId(rs.wasNull() ? null : evaluatorId);
        e.setStatus(rs.getString("status"));
        e.setScheduledDate(rs.getTimestamp("scheduled_date"));
        int grade = rs.getInt("grade");
        e.setGrade(rs.wasNull() ? null : grade);
        e.setComment(rs.getString("comments"));
        int finalGrade = rs.getInt("final_grade");
        e.setFinalGrade(rs.wasNull() ? null : finalGrade);
        int passedInt = rs.getInt("passed");
        if (rs.wasNull()) {
            e.setPassed(false);
        } else {
            e.setPassed(passedInt == 1);
        }
        return e;
    }
}