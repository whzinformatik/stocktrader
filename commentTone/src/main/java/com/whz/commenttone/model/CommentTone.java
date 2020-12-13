package com.whz.commenttone.model;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

import java.util.Random;

public final class CommentTonePublishedEvent extends IdentifiedDomainEvent {
    public final String id;
    public final String message;
    public final String sentiment;

    public CommentTone(String id, String message) {
        this.id = id;
        this.message = message;
        this.sentiment = sentiment;
    }

    @Override
    public String toString() {
        return "CommentTone{" +
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
