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

import com.whz.feedback.infrastructure.EventTypes;
import com.whz.feedback.infrastructure.FeedbackData;
import com.whz.feedback.model.feedback.FeedbackSubmitted;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

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
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          return Empty;
      }
    }

    return null;
  }
}
