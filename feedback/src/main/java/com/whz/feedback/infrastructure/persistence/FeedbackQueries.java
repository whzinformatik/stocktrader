/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.infrastructure.persistence;

import com.whz.feedback.infrastructure.FeedbackData;
import io.vlingo.common.Completes;
import java.util.Collection;

/**
 * This interface is used to declare all methods for feedback queries.
 * @since 1.0.0
 */
public interface FeedbackQueries {

  /**
   * Get a {@link FeedbackData} for a specific feedback message
   * @param id identifier of the feedback message
   * @return whole feedback message
   * @since 1.0.0
   */
  Completes<FeedbackData> feedbackOf(String id);

  /**
   * Get all feedback messages of all users
   * @return all feedback messages
   * @since 1.0.0
   */
  Completes<Collection<FeedbackData>> feedbacks();
}
