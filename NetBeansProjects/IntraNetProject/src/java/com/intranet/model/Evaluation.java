package com.intranet.model;

import java.util.Date;

public class Evaluation {
    private int id;
    private int projectId;
    private int userId;
    private Integer evaluatorId;
    private String status;
    private Date scheduledDate;
    private Integer grade;
    private String comments;
    private Integer finalGrade;
    private boolean passed;
    
    public Evaluation(){}

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the projectId
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the evaluatorId
     */
    public Integer getEvaluatorId() {
        return evaluatorId;
    }

    /**
     * @param evaluatorId the evaluatorId to set
     */
    public void setEvaluatorId(Integer evaluatorId) {
        this.evaluatorId = evaluatorId;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the scheduledDate
     */
    public Date getScheduledDate() {
        return scheduledDate;
    }

    /**
     * @param scheduledDate the scheduledDate to set
     */
    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    /**
     * @return the grade
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * @param grade the grade to set
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * @return the comments
     */
    public String getComment() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComment(String comments) {
        this.comments = comments;
    }

    /**
     * @return the finalGrade
     */
    public Integer getFinalGrade() {
        return finalGrade;
    }

    /**
     * @param finalGrade the finalGrade to set
     */
    public void setFinalGrade(Integer finalGrade) {
        this.finalGrade = finalGrade;
    }

    /**
     * @return the passed
     */
    public boolean getPassed() {
        return passed;
    }

    /**
     * @param passed the passed to set
     */
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}