package ru.rpuxa.bomjserver;

import java.io.Serializable;


@DontEditSerializable
public class Review implements Serializable {
    private final float rating;
    private final String rev;

    public Review(float rating, String rev) {
        this.rating = rating;
        this.rev = rev;
    }

    public float getRating() {
        return rating;
    }

    public String getRev() {
        return rev;
    }
}
