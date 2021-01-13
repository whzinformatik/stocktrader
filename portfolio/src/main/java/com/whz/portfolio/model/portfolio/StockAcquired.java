/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.model.portfolio;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

/**
 * Event class. Contains information for acquiring a new stock.
 * 
 * @since 1.0.0
 *
 */
public final class StockAcquired extends IdentifiedDomainEvent {

  public final String id;

  public final String symbol;
  public final long acquisitionMarketTime;
  public final int amount;
  public final double acquisitionMarketPrice;

  public StockAcquired(
      String id,
      String symbol,
      long acquisitionMarketTime,
      int amount,
      double acquisitionMarketPrice) {
    this.id = id;
    this.symbol = symbol;
    this.acquisitionMarketTime = acquisitionMarketTime;
    this.amount = amount;
    this.acquisitionMarketPrice = acquisitionMarketPrice;
  }

  @Override
  public String identity() {
    return id;
  }
}
