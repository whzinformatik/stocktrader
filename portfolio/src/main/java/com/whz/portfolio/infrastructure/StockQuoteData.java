/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.infrastructure;

/** Used as a Java class representation of the stock data from the stock quote project */
public class StockQuoteData {

  public final String symbol;
  public final String longName;
  public final String financialCurrency;
  public final double regularMarketPrice;
  public final long regularMarketTime;

  public StockQuoteData(
      String symbol,
      String longName,
      String financialCurrency,
      double regularMarketPrice,
      long regularMarketTime) {
    this.symbol = symbol;
    this.longName = longName;
    this.financialCurrency = financialCurrency;
    this.regularMarketPrice = regularMarketPrice;
    this.regularMarketTime = regularMarketTime;
  }

  @Override
  public String toString() {
    return "Stock with symbol: '" + symbol + "'";
  }
}
