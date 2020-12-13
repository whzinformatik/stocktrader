package com.whz.commenttone.model;

public final class CommentTone {
  public final String id;
  public final String message;
  public String sentiment;

  public CommentTone(String id, String message, String sentiment) {
    this.id = id;
    this.message = message;
    this.sentiment = sentiment;
  }

  public void setSentiment(String sentiment) {
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
}
