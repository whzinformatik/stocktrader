package com.whz.stockquote.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import com.whz.stockquote.infrastructure.EventTypes;
import com.whz.stockquote.infrastructure.StockQuoteData;

import io.vlingo.lattice.model.DomainEvent;
import io.vlingo.lattice.model.IdentifiedDomainEvent;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Entry;

public class StockQuoteProjectionActor extends StateStoreProjectionActor<StockQuoteData> {
  private static final StockQuoteData Empty = StockQuoteData.empty();

  private String dataId;
  private final List<IdentifiedDomainEvent> events;

  public StockQuoteProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
    this.events = new ArrayList<>(2);
  }

  @Override
  protected StockQuoteData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected String dataIdFor(final Projectable projectable) {
    dataId = events.get(0).identity();
    return dataId;
  }

  @Override
  protected StockQuoteData merge(
      final StockQuoteData previousData,
      final int previousVersion,
      final StockQuoteData currentData,
      final int currentVersion) {

    if (previousVersion == currentVersion) {
      return currentData;
    }

    for (final DomainEvent event : events) {
      switch (EventTypes.valueOf(event.typeName())) {
        case StockQuotePlaceholderDefined:
          return StockQuoteData.from(currentData.id, "Handle StockQuotePlaceholderDefined here");   // TODO: implement actual merge
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
