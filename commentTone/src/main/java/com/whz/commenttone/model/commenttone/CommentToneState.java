package com.whz.commenttone.model.commenttone;

import java.util.Random;

public final class CommentToneState {
    public final String id;
    public final String message;
    public String sentiment;

    private CommentToneState(final String id, final String value) {
        this.id = id;
        this.message = value;

        int r = new Random().nextInt(11);
        this.sentiment = r < 4 ? "negative" : r < 8 ? "neutral" : "positive";
    }

    private CommentToneState(final String id) {
        this(id, null);
    }

    private CommentToneState() {
        this(null, null);
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
        return new CommentToneState(this.id, value);
    }
}
