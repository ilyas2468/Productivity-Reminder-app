package com.example.reminders;

public class Reminder {
    private int id;
    private String text;
    private String source;
    private String reference;

    public Reminder(int id, String text, String source, String reference) {
        this.id = id;
        this.text = text;
        this.source = source;
        this.reference = reference;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}