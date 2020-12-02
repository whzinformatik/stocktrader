package com.whz.portfolio.model.portfolio;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class StockAcquired extends IdentifiedDomainEvent {

	public final String id;

	public final String symbol;
	public final long acquisitionMarketTime;
	public final int amount;

	public StockAcquired(String id, String symbol, long acquisitionMarketTime, int amount) {
		this.id = id;
		this.symbol = symbol;
		this.acquisitionMarketTime = acquisitionMarketTime;
		this.amount = amount;
	}

	@Override
	public String identity() {
		return id;
	}
}
