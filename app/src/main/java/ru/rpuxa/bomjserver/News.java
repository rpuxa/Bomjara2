package ru.rpuxa.bomjserver;

import java.io.Serializable;

@DontEditSerializable
public class News implements Serializable {
    private static final long serialVersionUID = -980646923792371827L;

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
