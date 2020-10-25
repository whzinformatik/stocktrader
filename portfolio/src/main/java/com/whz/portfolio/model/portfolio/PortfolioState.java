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

  public PortfolioState withPlaceholderValue(final String value) {
    return new PortfolioState(this.id, value);
  }

  private PortfolioState(final String id, final String value) {
    this.id = id;
    this.owner = value;
  }

  private PortfolioState(final String id) {
    this(id, null);
  }

  private PortfolioState() {
    this(null, null);
  }
}
