/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.model.feedback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FeedbackStateTest {

  private final String id = "new_id";

  private final String message = "important notice";

  private final String portfolioId = "portfolio_id";

  @Test
  public void testNullId() {
    FeedbackState feedbackState = FeedbackState.identifiedBy(null);
    assertNull(feedbackState.id);
    assertNull(feedbackState.message);
    assertNull(feedbackState.portfolioId);
    assertTrue(feedbackState.doesNotExist());
    assertFalse(feedbackState.isIdentifiedOnly());
  }

  @Test
  public void testOnlyId() {
    FeedbackState feedbackState = FeedbackState.identifiedBy(id);
    assertThat(feedbackState.id).isEqualTo(id);
    assertNull(feedbackState.message);
    assertNull(feedbackState.portfolioId);
    assertFalse(feedbackState.doesNotExist());
    assertTrue(feedbackState.isIdentifiedOnly());
  }

  @Test
  public void testIdAndMessage() {
    FeedbackState feedbackState =
        FeedbackState.identifiedBy(id).withMessage(message).withPortfolioId(portfolioId);
    assertThat(feedbackState.id).isEqualTo(id);
    assertThat(feedbackState.message).isEqualTo(message);
    assertThat(feedbackState.portfolioId).isEqualTo(portfolioId);
    assertFalse(feedbackState.doesNotExist());
    assertFalse(feedbackState.isIdentifiedOnly());
  }
}
