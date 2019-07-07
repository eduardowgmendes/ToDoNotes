package br.com.jet.app.todonotes.model;

import java.io.Serializable;

public class EmptyState implements Serializable {

    private int resDrawable;
    private String title, content;

    public EmptyState(int resDrawable, String title, String content) {
        this.resDrawable = resDrawable;
        this.title = title;
        this.content = content;
    }

    public int getResDrawable() {
        return resDrawable;
    }

    public void setResDrawable(int resDrawable) {
        this.resDrawable = resDrawable;
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
}
