package com.whz.portfolio.infrastructure;

/**
 * 
 * @author Florian
 * Used in the post request to add a new stock to the portfolio
 */
public class AcquireStockData {

	public final String symbol;
	public final int amount;

	public AcquireStockData(String symbol, String name, long acquisitionMarketTime, int amount,
			double acquisitionPrice, double currentPrice, String currency) {
		this.symbol = symbol;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "StockData [symbol=" + symbol + " amount=" + amount + "]";
	}

}
