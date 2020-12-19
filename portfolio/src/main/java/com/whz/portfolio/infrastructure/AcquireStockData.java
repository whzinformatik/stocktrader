package com.whz.portfolio.infrastructure;

/**
 * Java representation for the required JSON object in the post request for adding a new stock to
 * the portfolio. The symbol is the unique identifier for a stock.
 *
 * @author Florian
 */
public class AcquireStockData {

  public final String symbol;
  public final int amount;

  /**
   * The symbol is the unique identifier for a stock
   *
   * @param symbol
   * @param amount
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
