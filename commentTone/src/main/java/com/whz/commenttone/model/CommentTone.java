package com.whz.commenttone.model;

import java.util.Random;

public class CommentTone {
    public final String id;
    public final String message;
    public final String sentiment;

    public CommentTone(String id, String message) {
        this.id = id;
        this.message = message;

        int r = new Random().nextInt(11);
        this.sentiment = r < 4 ? "negative" : r < 8 ? "neutral" : "positive";
    }

    @Override
    public String toString() {
        return "CommentTone{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", sentiment='" + sentiment + '\'' +
                '}';
    }
}
