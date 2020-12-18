package com.whz.portfolio.infrastructure;

import com.whz.portfolio.model.portfolio.PortfolioState;
import com.whz.portfolio.model.portfolio.Stock;

import java.util.ArrayList;
import java.util.List;

public class PortfolioData {

	public String id;
	public String owner;
	public List<Stock> stocks;

	public static PortfolioData empty() {
		return new PortfolioData("", "", new ArrayList<>());
	}

	public static PortfolioData from(final PortfolioState state) {
		return new PortfolioData(state.id, state.owner, state.stocks);
	}

	public static List<PortfolioData> from(final List<PortfolioState> states) {
		final List<PortfolioData> data = new ArrayList<>(states.size());

		for (final PortfolioState state : states) {
			data.add(PortfolioData.from(state));
		}

		return data;
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
