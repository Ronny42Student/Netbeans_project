package com.intranet.model;

import java.util.Date;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private int currentLevel;
    private String campusLocation;
    private String role;
    private Date etaDate;
    private String avatarPath;

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = "USER";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
    public String getCampusLocation() { return campusLocation; }
    public void setCampusLocation(String campusLocation) { this.campusLocation = campusLocation; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Date getEtaDate() { return etaDate; }
    public void setEtaDate(Date etaDate) { this.etaDate = etaDate; }

    /**
     * @return the avatarPath
     */
    public String getAvatarPath() {
        return avatarPath;
    }

    /**
     * @param avatarPath the avatarPath to set
     */
    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
}