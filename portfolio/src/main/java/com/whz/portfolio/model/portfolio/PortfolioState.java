package com.whz.portfolio.model.portfolio;

public final class PortfolioState {
  public final String id;
  public final String owner;

  public static PortfolioState identifiedBy(final String id) {
    return new PortfolioState(id);
  }

  public boolean doesNotExist() {
    return id == null;
  }

  public boolean isIdentifiedOnly() {
    return id != null && owner == null;
  }

  public PortfolioState withOwner(final String owner) {
    return new PortfolioState(this.id, owner);
  }

  private PortfolioState(final String id, final String owner) {
    this.id = id;
    this.owner = owner;
  }

  private PortfolioState(final String id) {
    this(id, null);
  }

  private PortfolioState() {
    this(null, null);
  }
}
