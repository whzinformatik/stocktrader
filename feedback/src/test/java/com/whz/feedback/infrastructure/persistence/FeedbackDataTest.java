package com.whz.feedback.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.*;

import com.whz.feedback.infrastructure.FeedbackData;
import com.whz.feedback.model.feedback.FeedbackState;
import org.junit.jupiter.api.Test;

public class FeedbackDataTest {

  private final String id = "new_id";

  private final String message = "important notice";

  @Test
  public void testEmpty() {
    FeedbackData feedbackData = FeedbackData.empty();
    assertTrue(feedbackData.id.isEmpty());
    assertTrue(feedbackData.message.isEmpty());
  }

  @Test
  public void testFromStateWithNull() {
    FeedbackState feedbackState = FeedbackState.identifiedBy(null);
    FeedbackData feedbackData = FeedbackData.from(feedbackState);
    assertNull(feedbackData.id);
    assertNull(feedbackData.message);
  }

  @Test
  public void testFromStateNonNull() {
    FeedbackState feedbackState = FeedbackState.identifiedBy(id).withMessage(message);
    FeedbackData feedbackData = FeedbackData.from(feedbackState);
    assertEquals(id, feedbackData.id);
    assertEquals(message, feedbackData.message);
  }

  @Test
  public void testJust() {
    FeedbackData feedbackData = FeedbackData.just(message);
    assertTrue(feedbackData.id.isEmpty());
    assertEquals(message, feedbackData.message);
  }
}
