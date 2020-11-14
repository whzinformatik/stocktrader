package com.whz.portfolio.model.portfolio;

import com.whz.portfolio.infrastructure.persistence.CommandModelJournalProvider;
import io.vlingo.common.Completes;
import io.vlingo.common.Outcome;
import io.vlingo.lattice.model.sourcing.EventSourced;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.Source;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.journal.Journal.AppendResultInterest;
import java.util.List;
import java.util.Optional;

public final class PortfolioEntity extends EventSourced implements Portfolio {
  private PortfolioState state;
  private int version = 0;

  public PortfolioEntity(final String id) {
    super(id);
    this.state = PortfolioState.identifiedBy(id);
  }

  public Completes<PortfolioState> portfolioCreated(final String value) {

    return apply(new PortfolioCreated(state.id, value), () -> state);
  }

  // =====================================
  // EventSourced
  // =====================================

  static {
    EventSourced.registerConsumer(
        PortfolioEntity.class, PortfolioCreated.class, PortfolioEntity::applyPortfolioCreated);
  }

  private void applyPortfolioCreated(final PortfolioCreated e) {
    CommandModelJournalProvider.instance()
        .journal
        .append(
            "PortfolioCreated",
            version++,
            e,
            new AppendResultInterest() {
              @Override
              public <S, ST> void appendResultedIn(
                  Outcome<StorageException, Result> outcome,
                  String streamName,
                  int streamVersion,
                  Source<S> source,
                  Metadata metadata,
                  Optional<ST> snapshot,
                  Object object) {}

              @Override
              public <S, ST> void appendResultedIn(
                  Outcome<StorageException, Result> outcome,
                  String streamName,
                  int streamVersion,
                  Source<S> source,
                  Optional<ST> snapshot,
                  Object object) {}

              @Override
              public <S, ST> void appendAllResultedIn(
                  Outcome<StorageException, Result> outcome,
                  String streamName,
                  int streamVersion,
                  List<Source<S>> sources,
                  Metadata metadata,
                  Optional<ST> snapshot,
                  Object object) {}

              @Override
              public <S, ST> void appendAllResultedIn(
                  Outcome<StorageException, Result> outcome,
                  String streamName,
                  int streamVersion,
                  List<Source<S>> sources,
                  Optional<ST> snapshot,
                  Object object) {}
            },
            this);
    state = state.withOwner(e.owner);
  }
}
