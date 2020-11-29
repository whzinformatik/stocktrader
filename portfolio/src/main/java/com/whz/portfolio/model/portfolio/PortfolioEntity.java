package com.whz.portfolio.model.portfolio;

import com.whz.portfolio.infrastructure.StockQuoteData;
import com.whz.portfolio.resource.QuotesCache;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public final class PortfolioEntity extends EventSourced implements Portfolio {
	private PortfolioState state;

	public PortfolioEntity(final String id) {
		super(id);
		this.state = PortfolioState.identifiedBy(id);
	}

	@Override
	public Completes<PortfolioState> portfolioCreated(final String value) {
		return apply(new PortfolioCreated(state.id, value), () -> state);
	}

	@Override
	public Completes<PortfolioState> stockAcquired(final String symbol, final int amount) {
		return apply(new StockAcquired(state.id, symbol, amount), () -> state);
	}

	// =====================================
	// EventSourced
	// =====================================

	static {
		EventSourced.registerConsumer(PortfolioEntity.class, PortfolioCreated.class,
				PortfolioEntity::applyPortfolioCreated);
		EventSourced.registerConsumer(PortfolioEntity.class, StockAcquired.class, PortfolioEntity::applyStockAcquired);
	}

	private void applyPortfolioCreated(final PortfolioCreated e) {
		state = state.withOwner(e.owner);
	}

	private void applyStockAcquired(final StockAcquired e) {
		StockQuoteData sq = QuotesCache.INSTANCE.get(e.symbol);
		Stock stock = new Stock(e.symbol, sq.longName, sq.regularMarketTime, e.amount, sq.regularMarketPrice,
				sq.regularMarketPrice, sq.financialCurrency);
		state = state.withAcquiredStock(stock);
	}

}
