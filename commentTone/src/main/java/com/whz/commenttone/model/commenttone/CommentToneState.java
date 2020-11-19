package com.whz.commenttone.model.commenttone;

public final class CommentToneState {
    public final String id;
    public final String placeholderValue;

    private CommentToneState(final String id, final String value) {
        this.id = id;
        this.placeholderValue = value;
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
        return id != null && placeholderValue == null;
    }

    public CommentToneState withPlaceholderValue(final String value) {
        return new CommentToneState(this.id, value);
    }
}
