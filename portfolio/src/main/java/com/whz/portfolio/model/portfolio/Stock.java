package com.whz.portfolio.model.portfolio;

public class Stock {

	public final String symbol;
	public final long acquisitionMarketTime;
	public final int amount;
	public final double acquisitionMarketPrice;

	public Stock(String symbol, long acquisitionMarketTime, int amount, double acquisitionMarketPrice) {
		this.symbol = symbol;
		this.acquisitionMarketTime = acquisitionMarketTime;
		this.amount = amount;
		this.acquisitionMarketPrice = acquisitionMarketPrice;
	}

}
