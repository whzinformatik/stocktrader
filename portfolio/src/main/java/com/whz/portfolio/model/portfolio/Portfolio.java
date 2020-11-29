package com.whz.portfolio.model.portfolio;

import java.util.HashMap;
import java.util.Map;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Portfolio {
	
	static final Map<Long, Portfolio> cache = new HashMap<>();
	
	static Completes<PortfolioState> defineWith(final Stage stage, final String owner) {
		final Address address = stage.world().addressFactory().uniqueWith("test");
		final Portfolio portfolio = stage.actorFor(Portfolio.class,
				Definition.has(PortfolioEntity.class, Definition.parameters(address.idString())), address);
		cache.put(address.id(), portfolio);
		return portfolio.portfolioCreated(owner);
	}

	static Completes<PortfolioState> acquireStock(final Stage stage, final String id, final String symbol,
			final int amount) {
		
		final Portfolio portfolio = cache.get(Long.valueOf(id));
		
//		final Address address = stage.world().addressFactory().from(id, "test");
		
		return portfolio.stockAcquired(symbol, amount);
		
//		return stage.actorOf(PortfolioEntity.class, address)
//				.andThen((portfolio) -> portfolio.stockAcquired(symbol, amount)).andFinally();
	}

	Completes<PortfolioState> portfolioCreated(final String owner);

	Completes<PortfolioState> stockAcquired(final String symbol, final int amount);

}
