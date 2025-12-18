package com.example.reminders;

import java.util.ArrayList;
import java.util.Date;

public class Task {

    public static ArrayList<Task> taskArrayList = new ArrayList<>();
    public static String TASK_EDIT_EXTRA = "taskEdit";

    private int id;
    private String taskName;
    private String description;
    private Date deleted;
    private String prayer; // NEW: Prayer category

    public Task(int id, String taskName, String description, Date deleted, String prayer) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
        this.deleted = deleted;
        this.prayer = prayer;
    }

    public Task(int id, String taskName, String description, String prayer) {
        this.id = id;
        this.taskName = taskName;
        this.description = description;
        this.prayer = prayer;
        this.deleted = null;
    }

    public static Task getTaskForID(int passedTaskID) {
        for(Task task : taskArrayList){
            if(task.getId() == passedTaskID)
                return task;
        }
        return null;
    }

    public static ArrayList<Task> nonDeletedTasks(){
        ArrayList<Task> nonDeleted = new ArrayList<>();
        for (Task task: taskArrayList){
            if (task.getDeleted() == null)
                nonDeleted.add(task);
        }
        return nonDeleted;
    }

    // NEW: Get non-deleted tasks for a specific prayer
    public static ArrayList<Task> nonDeletedTasksForPrayer(String prayerName){
        ArrayList<Task> filtered = new ArrayList<>();
        for (Task task: taskArrayList){
            if (task.getDeleted() == null && prayerName.equals(task.getPrayer()))
                filtered.add(task);
        }
        return filtered;
    }

    // Getters and Setters
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

    public String getPrayer() {
        return prayer;
    }

    public void setPrayer(String prayer) {
        this.prayer = prayer;
    }
}