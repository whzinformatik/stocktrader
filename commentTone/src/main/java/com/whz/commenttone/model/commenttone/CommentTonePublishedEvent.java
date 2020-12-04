package com.whz.commenttone.model.commenttone;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

import java.util.Random;

public final class CommentTonePublishedEvent extends IdentifiedDomainEvent {
    public final String id;
    public final String message;
    public final String sentiment;

    public CommentTonePublishedEvent(final String id, final String message, String sentiment) {
        this.id = id;
        this.message = message;

        int r = new Random().nextInt(11);
        this.sentiment = r < 4 ? "negative" : r < 8 ? "neutral" : "positive";
    }

    @Override
    public String toString() {
        return "CommentTonePublishedEvent{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", sentiment='" + sentiment + '\'' +
                '}';
    }

    @Override
    public String identity() {
        return id;
    }
}
