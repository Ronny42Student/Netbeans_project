package com.intranet.dao;

import com.intranet.config.DBConnection;
import com.intranet.model.Project;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT id, name, slug, deadline_days, max_retries, description, min_grade, active " +
                     "FROM projects WHERE active = 1 ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                projects.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }

    public List<Project> getAllProjectsAdmin() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT id, name, slug, deadline_days, max_retries, description, min_grade, active " +
                     "FROM projects ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                projects.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }

    public Project getProjectById(int id) {
        String sql = "SELECT id, name, slug, deadline_days, max_retries, description, min_grade, active " +
                     "FROM projects WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createProject(Project p) {
        String sql = "INSERT INTO projects (name, slug, deadline_days, max_retries, description, min_grade, active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getSlug());
            ps.setInt(3, p.getDeadlineDays());
            ps.setInt(4, p.getMaxRetries());
            ps.setString(5, p.getDescription());
            ps.setInt(6, p.getMinGrade());
            ps.setInt(7, p.isActive() ? 1 : 0);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProject(Project p) {
        String sql = "UPDATE projects SET name = ?, slug = ?, deadline_days = ?, max_retries = ?, " +
                     "description = ?, min_grade = ?, active = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getSlug());
            ps.setInt(3, p.getDeadlineDays());
            ps.setInt(4, p.getMaxRetries());
            ps.setString(5, p.getDescription());
            ps.setInt(6, p.getMinGrade());
            ps.setInt(7, p.isActive() ? 1 : 0);
            ps.setInt(8, p.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deactivateProject(int id) {
        String sql = "UPDATE projects SET active = 0 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProjectPermanently(int id) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private Project mapRow(ResultSet rs) throws SQLException {
        Project p = new Project();
        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setSlug(rs.getString("slug"));
        p.setDeadlineDays(rs.getInt("deadline_days"));
        p.setMaxRetries(rs.getInt("max_retries"));
        p.setDescription(rs.getString("description"));
        p.setMinGrade(rs.getInt("min_grade"));
        p.setActive(rs.getInt("active") == 1);
        return p;
    }
}