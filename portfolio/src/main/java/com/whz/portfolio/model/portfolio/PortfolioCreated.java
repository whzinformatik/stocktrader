package com.whz.portfolio.model.portfolio;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class PortfolioCreated extends IdentifiedDomainEvent {
  public final String id;
  public final String owner;

  public PortfolioCreated(final String id, final String owner) {
    this.id = id;
    this.owner = owner;
  }

  @Override
  public String identity() {
    return id;
  }
}
