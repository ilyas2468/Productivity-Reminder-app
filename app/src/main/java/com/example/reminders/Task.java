package com.example.reminders;

import java.util.ArrayList;
import java.util.Date;

public class Task {

    public static ArrayList<Task> taskArrayList = new ArrayList<>();
    private int id;
    private String taskName;
    private String description;
    private Date deleted;

    public Task(int id, String taskName, String description, Date deleted) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
        this.deleted = deleted;
    }

    public Task(int id, String taskName, String description) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
        deleted = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }
}
