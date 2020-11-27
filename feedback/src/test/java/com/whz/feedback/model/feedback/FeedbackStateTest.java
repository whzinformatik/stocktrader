/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.model.feedback;

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
