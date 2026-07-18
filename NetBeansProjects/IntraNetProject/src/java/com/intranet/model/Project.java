package com.intranet.model;

public class Project {
    private int id;
    private String name;
    private String slug;
    private int deadlineDays;
    private int maxRetries;
    private String description;
    private int minGrade;
    private boolean active;

    public Project() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public int getDeadlineDays() { return deadlineDays; }
    public void setDeadlineDays(int deadlineDays) { this.deadlineDays = deadlineDays; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getMinGrade() { return minGrade; }
    public void setMinGrade(int minGrade) { this.minGrade = minGrade; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}