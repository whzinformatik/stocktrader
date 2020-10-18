package com.whz.portfolio.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import com.whz.portfolio.infrastructure.EventTypes;
import com.whz.portfolio.infrastructure.PortfolioData;

import io.vlingo.lattice.model.DomainEvent;
import io.vlingo.lattice.model.IdentifiedDomainEvent;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Entry;

public class PortfolioProjectionActor extends StateStoreProjectionActor<PortfolioData> {
  private static final PortfolioData Empty = PortfolioData.empty();

  private String dataId;
  private final List<IdentifiedDomainEvent> events;

  public PortfolioProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
    this.events = new ArrayList<>(2);
  }

  @Override
  protected PortfolioData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected String dataIdFor(final Projectable projectable) {
    dataId = events.get(0).identity();
    return dataId;
  }

  @Override
  protected PortfolioData merge(
      final PortfolioData previousData,
      final int previousVersion,
      final PortfolioData currentData,
      final int currentVersion) {

    if (previousVersion == currentVersion) {
      return currentData;
    }

    for (final DomainEvent event : events) {
      switch (EventTypes.valueOf(event.typeName())) {
        case PortfolioPlaceholderDefined:
          return PortfolioData.from(currentData.id, "Handle PortfolioPlaceholderDefined here");   // TODO: implement actual merge
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

    for (Entry <?> entry : projectable.entries()) {
      events.add(entryAdapter().anyTypeFromEntry(entry));
    }
  }
}
