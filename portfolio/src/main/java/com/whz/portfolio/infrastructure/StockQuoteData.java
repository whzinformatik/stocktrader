package com.whz.portfolio.infrastructure;

public class StockQuoteData {

	public final String symbol;
	public final String displayName;

	private StockQuoteData(final String symbol, final String displayName) {
		this.symbol = symbol;
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return "Stock quote - symbol '" + symbol + "'";
	}

}
