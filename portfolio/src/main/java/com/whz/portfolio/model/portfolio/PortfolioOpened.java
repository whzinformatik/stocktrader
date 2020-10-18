package com.whz.portfolio.model.portfolio;

import io.vlingo.lattice.model.DomainEvent;

public final class PortfolioOpened extends DomainEvent {
  public final String id;
  public final String placeholderValue;

  public PortfolioOpened(final String id, final String placeholderValue) {
    this.id = id;
    this.placeholderValue = placeholderValue;
  }
}
