package com.whz.portfolio.model.portfolio;

import io.vlingo.lattice.model.DomainEvent;

public final class PortfolioCreated extends DomainEvent {
  public final String id;
  public final String owner;

  public PortfolioCreated(final String id, final String owner) {
    this.id = id;
    this.owner = owner;
  }
}
