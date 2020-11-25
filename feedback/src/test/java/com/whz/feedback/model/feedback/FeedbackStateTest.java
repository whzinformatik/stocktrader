package com.whz.feedback.model.feedback;

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

import org.junit.jupiter.api.Test;

public class FeedbackStateTest {

  private final String id = "new_id";

  private final String message = "important notice";

  @Test
  public void testNullId() {
    FeedbackState feedbackState = FeedbackState.identifiedBy(null);
    assertNull(feedbackState.id);
    assertNull(feedbackState.message);
    assertTrue(feedbackState.doesNotExist());
    assertFalse(feedbackState.isIdentifiedOnly());
  }

  @Test
  public void testOnlyId() {
    FeedbackState feedbackState = FeedbackState.identifiedBy(id);
    assertEquals(id, feedbackState.id);
    assertNull(feedbackState.message);
    assertFalse(feedbackState.doesNotExist());
    assertTrue(feedbackState.isIdentifiedOnly());
  }

  @Test
  public void testIdAndMessage() {
    FeedbackState feedbackState = FeedbackState.identifiedBy(id).withMessage(message);
    assertEquals(id, feedbackState.id);
    assertEquals(message, feedbackState.message);
    assertFalse(feedbackState.doesNotExist());
    assertFalse(feedbackState.isIdentifiedOnly());
  }
}
