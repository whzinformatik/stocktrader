package com.whz.portfolio.model.portfolio;

public class Stock {

	public final String symbol;
	public final long acquisitionMarketTime;
	public final int amount;

	public Stock(String symbol, long acquisitionMarketTime, int amount) {
		this.symbol = symbol;
		this.acquisitionMarketTime = acquisitionMarketTime;
		this.amount = amount;
	}

}
