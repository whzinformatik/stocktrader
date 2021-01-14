/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.model.portfolio;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

/**
 * Implementation of the Portfolio interface. The Events have to be registered in the static
 * section.
 *
 * @since 1.0.0
 */
public final class PortfolioEntity extends EventSourced implements Portfolio {
  private PortfolioState state;

  public PortfolioEntity(final String id) {
    super(id);
    this.state = PortfolioState.identifiedBy(id);
  }

  /**
   * Triggers the PortfolioCreated event.
   *
   * @param owner
   * @return PortfolioState instance
   * @since 1.0.0
   */
  @Override
  public Completes<PortfolioState> portfolioCreated(final String value) {
    return apply(new PortfolioCreated(state.id, value), () -> state);
  }

  /**
   * Triggers the StockAcquired event.
   *
   * @param symbol
   * @param acquisitionMarketTime
   * @param amount
   * @param acquisitionMarketPrice
   * @return PortfolioState instance
   * @since 1.0.0
   */
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
