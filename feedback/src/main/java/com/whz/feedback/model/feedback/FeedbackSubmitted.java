package com.whz.feedback.model.feedback;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class FeedbackSubmittedEvent extends IdentifiedDomainEvent {

  public final String id;
  public final String message;

  public FeedbackSubmittedEvent(final String id, final String message) {
    this.id = id;
    this.message = message;
  }

  @Override
  public String identity() {
    return id;
  }
}
