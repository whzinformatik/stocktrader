package com.whz.portfolio.model.portfolio;

import java.util.Collections;
import java.util.List;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public final class PortfolioEntity extends EventSourced implements Portfolio {
  private PortfolioState state;

  public PortfolioEntity(final String id) {
    super(id);
    this.state = PortfolioState.identifiedBy(id);
  }

  public Completes<PortfolioState> portfolioOpened(final String value) {
    return apply(new PortfolioOpened(state.id, value), () -> state);
  }

  //=====================================
  // EventSourced
  //=====================================

  static {
    EventSourced.registerConsumer(PortfolioEntity.class, PortfolioOpened.class, PortfolioEntity::applyPortfolioPlaceholderDefined);
  }

  private void applyPortfolioPlaceholderDefined(final PortfolioOpened e) {
    state = state.withPlaceholderValue(e.placeholderValue);
  }
}
