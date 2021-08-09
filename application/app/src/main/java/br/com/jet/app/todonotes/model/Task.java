package br.com.jet.app.todonotes.model;

import java.io.Serializable;

public class Task implements Serializable {

    public static final int HIGH = 0;
    public static final int MEDIUM = 1;
    public static final int LOW = 2;

    private long id;
    private int priority;
    private String title, content;

    public Task(int priority, String title, String content) {
        this.priority = priority;
        this.title = title;
        this.content = content;
    }

    public Task(long id, int priority, String title, String content) {
        this.id = id;
        this.priority = priority;
        this.title = title;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
}
