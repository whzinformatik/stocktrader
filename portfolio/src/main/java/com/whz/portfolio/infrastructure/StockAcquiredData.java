package com.whz.portfolio.infrastructure;

/**
 * Class for StockAcquiredPublisher. Used to notify the account about a stock
 * purchase.
 * 
 * @since 1.0.0
 */
public class StockAcquiredData {

	public String owner;
	public double value;

	public StockAcquiredData(String owner, double value) {
		this.owner = owner;
		this.value = value;
	}

}
