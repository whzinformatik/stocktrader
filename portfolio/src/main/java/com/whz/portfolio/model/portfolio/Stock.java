package com.whz.portfolio.model.portfolio;

public class Stock {
	
	public final String symbol;
	public final String name;
	public final long acquisitionMarketTime;
	public final int amount;
	public final double acquisitionPrice;
	public final double currentPrice;
	public final String currency;

	public Stock(String symbol, String name, long acquisitionMarketTime, int amount, double acquisitionPrice,
			double currentPrice, String currency) {
		this.symbol = symbol;
		this.name = name;
		this.acquisitionMarketTime = acquisitionMarketTime;
		this.amount = amount;
		this.acquisitionPrice = acquisitionPrice;
		this.currentPrice = currentPrice;
		this.currency = currency;
	}

}
