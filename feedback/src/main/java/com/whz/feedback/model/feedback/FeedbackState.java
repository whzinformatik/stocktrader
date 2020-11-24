package com.whz.feedback.model.feedback;

public final class FeedbackState {

  public final String id;

  public final String message;

  public static FeedbackState identifiedBy(final String id) {
    return new FeedbackState(id);
  }

  public boolean doesNotExist() {
    return id == null;
  }

  public boolean isIdentifiedOnly() {
    return id != null && message == null;
  }

  public FeedbackState withMessage(final String message) {
    return new FeedbackState(this.id, message);
  }

  private FeedbackState(final String id, final String message) {
    this.id = id;
    this.message = message;
  }

  private FeedbackState(final String id) {
    this(id, null);
  }

  private FeedbackState() {
    this(null, null);
  }
}
