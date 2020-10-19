package com.whz.account.infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import com.whz.account.infrastructure.EventTypes;
import com.whz.account.infrastructure.AccountData;

import io.vlingo.lattice.model.DomainEvent;
import io.vlingo.lattice.model.IdentifiedDomainEvent;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Entry;

public class AccountProjectionActor extends StateStoreProjectionActor<AccountData> {
  private static final AccountData Empty = AccountData.empty();

  private String dataId;
  private final List<IdentifiedDomainEvent> events;

  public AccountProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
    this.events = new ArrayList<>(2);
  }

  @Override
  protected AccountData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  @Override
  protected String dataIdFor(final Projectable projectable) {
    dataId = events.get(0).identity();
    return dataId;
  }

  @Override
  protected AccountData merge(
      final AccountData previousData,
      final int previousVersion,
      final AccountData currentData,
      final int currentVersion) {

    if (previousVersion == currentVersion) {
      return currentData;
    }

    for (final DomainEvent event : events) {
      switch (EventTypes.valueOf(event.typeName())) {
        case AccountPlaceholderDefined:
          return AccountData.from(currentData.id, "Handle AccountPlaceholderDefined here");   // TODO: implement actual merge
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
