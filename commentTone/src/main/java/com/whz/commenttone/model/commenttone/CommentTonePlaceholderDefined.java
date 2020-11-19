package com.whz.commenttone.model.commenttone;

import io.vlingo.lattice.model.DomainEvent;

public final class CommentTonePlaceholderDefined extends DomainEvent {
    public final String id;
    public final String placeholderValue;

    public CommentTonePlaceholderDefined(final String id, final String placeholderValue) {
        this.id = id;
        this.placeholderValue = placeholderValue;
    }
}
