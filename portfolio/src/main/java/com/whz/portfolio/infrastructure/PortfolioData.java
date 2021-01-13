/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.infrastructure;

import com.whz.portfolio.model.portfolio.PortfolioState;
import com.whz.portfolio.model.portfolio.Stock;
import java.util.ArrayList;
import java.util.List;

/**
 * Java representation for the portfolio JSON object.
 * 
 * @since 1.0.0
 */
public class PortfolioData {

	public String id;
	public String owner;
	public List<Stock> stocks;

	public static PortfolioData empty() {
		return new PortfolioData("", "", new ArrayList<>());
	}

	/**
	 * Creates a PortfolioData instance with the data from the state param.
	 * 
	 * @param state
	 * @return New PortfolioData instance
	 * @since 1.0.0
	 */
	public static PortfolioData from(final PortfolioState state) {
		return new PortfolioData(state.id, state.owner, state.stocks);
	}

	/**
	 * Creates a list of PortfolioData instances with the data from the list of
	 * states param.
	 * 
	 * @param state
	 * @return New PortfolioData instance
	 * @since 1.0.0
	 */
	public static List<PortfolioData> from(final List<PortfolioState> states) {
		final List<PortfolioData> data = new ArrayList<>(states.size());

		for (final PortfolioState state : states) {
			data.add(PortfolioData.from(state));
		}

		return data;
	}

	/**
	 * Creates a new {@link PortfolioData} instance with the given owner.
	 * 
	 * @param owner
	 * @return New {@link PortfolioData} instance
	 * @since 1.0.0
	 */
	public static PortfolioData just(String owner) {
		return new PortfolioData("", owner, new ArrayList<>());
	}

	@Override
	public String toString() {
		return "PortfolioData [id=" + id + ", owner=" + owner + ", stocks=" + stocks.size() + "]";
	}

	private PortfolioData(final String id, final String owner, final List<Stock> stocks) {
		this.id = id;
		this.owner = owner;
		this.stocks = stocks;
	}
}
