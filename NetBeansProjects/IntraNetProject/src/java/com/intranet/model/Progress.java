package com.intranet.model;

import java.util.Date;

public class Progress {
    private int userId;
    private int projectId;
    private String status;
    private Integer grade;
    private int attemptNumber;
    private double penaltyPercent;
    private Date enrolledAt;
    private Date deadline;
    private Date failedAt;

    private String projectName;
    private String username;

    public Progress() {}

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getGrade() { return grade; }
    public void setGrade(Integer grade) { this.grade = grade; }
    public int getAttemptNumber() { return attemptNumber; }
    public void setAttemptNumber(int attemptNumber) { this.attemptNumber = attemptNumber; }
    public double getPenaltyPercent() { return penaltyPercent; }
    public void setPenaltyPercent(double penaltyPercent) { this.penaltyPercent = penaltyPercent; }
    public Date getEnrolledAt() { return enrolledAt; }
    public void setEnrolledAt(Date enrolledAt) { this.enrolledAt = enrolledAt; }
    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }
    public Date getFailedAt() { return failedAt; }
    public void setFailedAt(Date failedAt) { this.failedAt = failedAt; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}