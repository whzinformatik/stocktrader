package com.whz.feedback.infrastructure.persistence;

/*-
 * #%L
 * feedback
 * %%
 * Copyright (C) 2020 Fachgruppe Informatik WHZ
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

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
