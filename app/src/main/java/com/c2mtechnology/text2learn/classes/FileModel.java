package com.c2mtechnology.text2learn.classes;

public class FileModel {
    String name,text;

    public FileModel() {
        this.name = null;
        this.text = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
