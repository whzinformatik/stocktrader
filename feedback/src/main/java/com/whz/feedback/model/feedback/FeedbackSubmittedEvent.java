package com.whz.feedback.model.feedback;

import io.vlingo.lattice.model.DomainEvent;

public final class FeedbackSubmittedEvent extends DomainEvent {

    public final String id;
    public final String message;

    public FeedbackSubmittedEvent(final String id, final String message) {
        this.id = id;
        this.message = message;
    }
}
