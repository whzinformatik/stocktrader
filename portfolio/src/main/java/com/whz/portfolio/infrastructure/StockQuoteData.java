package com.whz.portfolio.infrastructure;

/**
 * Used as a Java class representation of the stock data from the stock quote project
 */
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
