package com.whz.portfolio.infrastructure.persistence;

import com.whz.portfolio.infrastructure.EventTypes;
import com.whz.portfolio.infrastructure.PortfolioData;
import io.vlingo.lattice.model.DomainEvent;
import io.vlingo.lattice.model.IdentifiedDomainEvent;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.Source;
import java.util.ArrayList;
import java.util.List;

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
      PortfolioData previousData,
      int previousVersion,
      PortfolioData currentData,
      int currentVersion) {
    return super.merge(previousData, previousVersion, currentData, currentVersion);
  }

  @Override
  protected PortfolioData merge(
      final PortfolioData previousData,
      final int previousVersion,
      final PortfolioData currentData,
      final int currentVersion,
      final List<Source<?>> sources) {

    if (previousVersion == currentVersion) {
      return currentData;
    }

    for (final DomainEvent event : events) {
      switch (EventTypes.valueOf(event.typeName())) {
        case PortfolioCreated:
          return PortfolioData.from(currentData.id, currentData.owner); // fixed and closed
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
