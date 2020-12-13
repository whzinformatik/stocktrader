package com.whz.commenttone.model.commenttone;

import java.util.Random;

public final class CommentToneState {
    public final String id;
    public final String message;
    public final String sentiment;

    private CommentToneState(final String id, final String message, final String sentiment) {
        this.id = id;
        this.message = message;

        int r = new Random().nextInt(11);
        this.sentiment = r < 4 ? "negative" : r < 8 ? "neutral" : "positive";
    }

    private CommentToneState(final String id) {
        this(id, null, null);
    }

    private CommentToneState() {
        this(null, null, null);
    }

    public static CommentToneState identifiedBy(final String id) {
        return new CommentToneState(id);
    }

    public boolean doesNotExist() {
        return id == null;
    }

    public boolean isIdentifiedOnly() {
        return id != null && message == null;
    }

    public CommentToneState withMessage(final String value) {
        return new CommentToneState(this.id, value, sentiment);
    }

    @Override
    public String toString() {
        return "CommentToneState{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", sentiment='" + sentiment + '\'' +
                '}';
    }
}
