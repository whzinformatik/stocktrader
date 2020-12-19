/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.infrastructure.persistence;

import com.whz.feedback.infrastructure.EventTypes;
import com.whz.feedback.infrastructure.FeedbackData;
import com.whz.feedback.model.feedback.FeedbackSubmitted;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

/**
 * This actor class is used to save an instance of {@link FeedbackData} into the StateStore.
 *
 * @since 1.0.0
 */
public class FeedbackProjectionActor extends StateStoreProjectionActor<FeedbackData> {

  private static final FeedbackData Empty = FeedbackData.empty();

  public FeedbackProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  @Override
  protected FeedbackData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected FeedbackData merge(
      final FeedbackData previousData,
      final int previousVersion,
      final FeedbackData currentData,
      final int currentVersion) {

    for (final Source<?> event : sources()) {
      switch (EventTypes.valueOf(event.typeName())) {
        case FeedbackSubmitted:
          final FeedbackSubmitted feedbackSubmittedEvent = typed(event);
          return FeedbackData.from(feedbackSubmittedEvent.id, feedbackSubmittedEvent.message);
        default:
          logger().warn("Event of type {} was not matched.", event.typeName());
          return Empty;
      }
    }

    return null;
  }
}
