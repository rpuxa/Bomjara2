package ru.rpuxa.bomjserver;

import java.io.Serializable;

/**
 * DONT EDIT
 */
public class News implements Serializable {
    private final String text;
    private final int date;

    public News(String text, int date) {
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public int getDate() {
        return date;
    }
}
