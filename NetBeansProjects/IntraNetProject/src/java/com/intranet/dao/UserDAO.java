/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.intranet.dao;

import com.intranet.config.DBConnection;
import com.intranet.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author nrajaoar
 */
public class UserDAO {
    public boolean userExists(String username, String email) {
        String sql = "SELECT 1 FROM users WHERE username = ? OR email = ?";
        try(
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        )
        {
            ps.setString(1, username);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery())
            {
                return rs.next();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean register(User user)
    {
        if(userExists(user.getUsername(), user.getEmail()))
        {
            return false;
        }
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)
        )
        {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            int rowAffected = ps.executeUpdate();
            return rowAffected > 0;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    public User login(String username, String password)
    {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try(
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        )
        {
            ps.setString(1, username);
            ps.setString(2, password);
            try(ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    return user;
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
