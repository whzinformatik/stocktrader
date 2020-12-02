package com.whz.commenttone.model.commenttone;

import io.vlingo.lattice.model.DomainEvent;

public final class CommentTonePublishedEvent extends DomainEvent {
    public final String id;
    public final String message;
    public final String sentiment;

    public CommentTonePublishedEvent(final String id, final String message, String sentiment) {
        this.id = id;
        this.message = message;
        this.sentiment = sentiment;
    }

    @Override
    public String toString() {
        return "CommentTonePublishedEvent{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", sentiment='" + sentiment + '\'' +
                '}';
    }
}
