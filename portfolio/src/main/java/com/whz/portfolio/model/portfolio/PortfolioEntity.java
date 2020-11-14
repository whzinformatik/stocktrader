package com.whz.portfolio.model.portfolio;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public final class PortfolioEntity extends EventSourced implements Portfolio {
  private PortfolioState state;

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
    state = state.withOwner(e.owner);
  }
}
