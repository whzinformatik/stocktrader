/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.infrastructure;

/**
 * Java representation for the required JSON object in the post request for adding a new stock to
 * the portfolio. The symbol is the unique identifier for a stock.
 *
 * @since 1.0.0
 */
public class AcquireStockData {

  public final String symbol;
  public final int amount;

  /**
   * Create an event if a stock is acquired.
   *
   * @param symbol unique identifier for a stock
   * @param amount Number of shares purchased
   */
  public AcquireStockData(String symbol, int amount) {
    this.symbol = symbol;
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "AcquireStockData [symbol=" + symbol + ", amount=" + amount + "]";
  }
}
