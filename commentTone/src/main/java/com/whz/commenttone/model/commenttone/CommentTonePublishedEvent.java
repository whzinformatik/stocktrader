package com.whz.commenttone.model.commenttone;

import io.vlingo.lattice.model.DomainEvent;

public final class CommentTonePublishedEvent extends DomainEvent {
    public final String id;
    public final String message;

    public CommentTonePublishedEvent(final String id, final String message) {
        this.id = id;
        this.message = message;
    }
}
