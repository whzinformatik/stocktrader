/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.whz.feedback.infrastructure.FeedbackData;
import com.whz.feedback.model.feedback.FeedbackState;
import org.junit.jupiter.api.Test;

public class FeedbackDataTest {

  private final String id = "new_id";

  private final String message = "important notice";

  @Test
  public void testEmpty() {
    FeedbackData feedbackData = FeedbackData.empty();
    assertThat(feedbackData.id).isEmpty();
    assertThat(feedbackData.message).isEmpty();
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
    assertThat(feedbackData.id).isEqualTo(id);
    assertThat(feedbackData.message).isEqualTo(message);
  }

  @Test
  public void testJust() {
    FeedbackData feedbackData = FeedbackData.just(message);
    assertThat(feedbackData.id).isEmpty();
    assertThat(feedbackData.message).isEqualTo(message);
  }
}
