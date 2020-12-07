package com.whz.portfolio.infrastructure;

/**
 * 
 * @author Florian Used in the post request to add a new stock to the portfolio
 */
public class AcquireStockData {

	public final String symbol;
	public final int amount;

	public AcquireStockData(String symbol, long acquisitionMarketTime, int amount) {
		this.symbol = symbol;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "AcquireStockData [symbol=" + symbol + ", amount=" + amount + "]";
	}

}
