package com.whz.commenttone.model;

import java.util.Random;

public class Feedback {
    public final String id;
    public final String message;

    public Feedback(String id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
