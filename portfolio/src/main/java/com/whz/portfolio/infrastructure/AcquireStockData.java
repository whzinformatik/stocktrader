package com.whz.portfolio.infrastructure;

/**
 * 
 * @author Florian Used in the post request to add a new stock to the portfolio
 */
public class AcquireStockData {

	public final String symbol;
	public final long acquisitionMarketTime;
	public final int amount;

	// String name
	// double acquisitionPrice
	// double currentPrice
	// String currency

	public AcquireStockData(String symbol, long acquisitionMarketTime, int amount) {
		this.symbol = symbol;
		this.acquisitionMarketTime = acquisitionMarketTime;
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "AcquireStockData [symbol=" + symbol + ", acquisitionMarketTime=" + acquisitionMarketTime + ", amount="
				+ amount + "]";
	}

}
