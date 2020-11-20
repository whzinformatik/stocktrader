package com.whz.feedback.infrastructure.persistence;

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
