package com.whz.portfolio.model.portfolio;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class StockAcquired extends IdentifiedDomainEvent {

	public final String id;

	public final String symbol;
	public final int amount;

	public StockAcquired(String id, String symbol, int amount) {
		this.id = id;
		this.symbol = symbol;
		this.amount = amount;
	}

	@Override
	public String identity() {
		return id;
	}
}
