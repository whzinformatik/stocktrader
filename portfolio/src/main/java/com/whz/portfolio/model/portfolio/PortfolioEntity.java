package com.whz.portfolio.model.portfolio;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public final class PortfolioEntity extends EventSourced implements Portfolio {
  private PortfolioState state;

  public PortfolioEntity(final String id) {
    super(id);
    this.state = PortfolioState.identifiedBy(id);
  }

  @Override
  public Completes<PortfolioState> portfolioCreated(final String value) {
    return apply(new PortfolioCreated(state.id, value), () -> state);
  }

  @Override
  public Completes<PortfolioState> stockAcquired(
      String symbol, long acquisitionMarketTime, int amount, double acquisitionMarketPrice) {
    return apply(
        new StockAcquired(state.id, symbol, acquisitionMarketTime, amount, acquisitionMarketPrice),
        () -> state);
  }

  // =====================================
  // EventSourced
  // =====================================

  static {
    EventSourced.registerConsumer(
        PortfolioEntity.class, PortfolioCreated.class, PortfolioEntity::applyPortfolioCreated);
    EventSourced.registerConsumer(
        PortfolioEntity.class, StockAcquired.class, PortfolioEntity::applyStockAcquired);
  }

  private void applyPortfolioCreated(final PortfolioCreated e) {
    state = state.withOwner(e.owner);
  }

  private void applyStockAcquired(final StockAcquired e) {
    Stock stock = new Stock(e.symbol, e.acquisitionMarketTime, e.amount, e.acquisitionMarketPrice);
    state = state.withAcquiredStock(stock);
  }
}
