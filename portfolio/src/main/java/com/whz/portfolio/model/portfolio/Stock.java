/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.model.portfolio;

/**
 * Class containing the stock information. The symbol is a unique identifier.
 * 
 * @since 1.0.0
 *
 */
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
