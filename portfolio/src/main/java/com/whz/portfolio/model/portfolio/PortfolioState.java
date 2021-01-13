/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.model.portfolio;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing the current Portfolio information.
 * 
 * @since 1.0.0
 *
 */
public final class PortfolioState {

	public final String id;
	public final String owner;
	public final List<Stock> stocks;

	/**
	 * Creates a new PortfolioState instance with a given id.
	 * 
	 * @param id
	 * @return New PortfolioState instance
	 * @since 1.0.0
	 */
	public static PortfolioState identifiedBy(final String id) {
		return new PortfolioState(id);
	}

	/**
	 * 
	 * @return True if the id is set. False if id is null.
	 * @since 1.0.0
	 */
	public boolean doesNotExist() {
		return id == null;
	}

	/**
	 * 
	 * @return True if all attributes are set. False if any attribute is not set.
	 * @since 1.0.0
	 */
	public boolean isIdentifiedOnly() {
		return id != null && owner == null && stocks == null;
	}

	/**
	 * 
	 * @param owner
	 * @return New PortfolioState instance with given owner.
	 * @since 1.0.0
	 */
	public PortfolioState withOwner(final String owner) {
		return new PortfolioState(id, owner, new ArrayList<>());
	}

	/**
	 * 
	 * @param stock
	 * @return New {@link PortfolioState} instance containing the given stock.
	 * @since 1.0.0
	 */
	public PortfolioState withAcquiredStock(final Stock stock) {
		PortfolioState state = new PortfolioState(id, owner, stocks);
		state.stocks.add(stock);
		return state;
	}

	private PortfolioState(final String id, final String owner, final List<Stock> stocks) {
		this.id = id;
		this.owner = owner;
		this.stocks = stocks;
	}

	private PortfolioState(final String id) {
		this(id, null, null);
	}

	private PortfolioState() {
		this(null, null, null);
	}
}
