package com.whz.feedback.infrastructure.persistence;

import com.whz.feedback.infrastructure.EventTypes;
import com.whz.feedback.infrastructure.FeedbackData;
import io.vlingo.lattice.model.DomainEvent;
import io.vlingo.lattice.model.IdentifiedDomainEvent;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Entry;

import java.util.ArrayList;
import java.util.List;

public class FeedbackProjectionActor extends StateStoreProjectionActor<FeedbackData> {
	
  private static final FeedbackData Empty = FeedbackData.empty();

  private String dataId;
  private final List<IdentifiedDomainEvent> events;

  public FeedbackProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
    this.events = new ArrayList<>(2);
  }

  @Override
  protected FeedbackData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected String dataIdFor(final Projectable projectable) {
    dataId = events.get(0).identity();
    return dataId;
  }

  @Override
  protected FeedbackData merge(
      final FeedbackData previousData,
      final int previousVersion,
      final FeedbackData currentData,
      final int currentVersion) {

    if (previousVersion == currentVersion) {
      return currentData;
    }

    for (final DomainEvent event : events) {
      switch (EventTypes.valueOf(event.typeName())) {
        case FeedbackSubmitted:
          return FeedbackData.from(previousData.id, currentData.message);
      default:
        logger().warn("Event of type " + event.typeName() + " was not matched.");
        break;
      }
    }

    return previousData;
  }

  @Override
  protected void prepareForMergeWith(final Projectable projectable) {
    events.clear();

    for (Entry<?> entry : projectable.entries()) {
      events.add(entryAdapter().anyTypeFromEntry(entry));
    }
  }
}
