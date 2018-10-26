package ru.rpuxa.bomjserver;

import java.io.Serializable;

public class ServerCommand implements Serializable {
    private final int id;
    private final Object data;

    public ServerCommand(int id, Object data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public Object getData() {
        return data;
    }
}