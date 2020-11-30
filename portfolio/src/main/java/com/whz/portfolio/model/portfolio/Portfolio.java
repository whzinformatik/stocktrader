package com.whz.portfolio.model.portfolio;

import java.util.HashMap;
import java.util.Map;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Portfolio {
	
	static Completes<PortfolioState> defineWith(final Stage stage, final String owner) {
		final Address address = stage.world().addressFactory().uniquePrefixedWith("p-");
		final Portfolio portfolio = stage.actorFor(Portfolio.class,
				Definition.has(PortfolioEntity.class, Definition.parameters(address.idString())), address);
		return portfolio.portfolioCreated(owner);
	}

//	static Completes<PortfolioState> acquireStock(final Stage stage, final String id, final String symbol,
//			final int amount) {
//		final Address address = stage.world().addressFactory().from("-p" + id);
//		return stage.actorOf(PortfolioEntity.class, address)
//				.andThen((portfolio) -> portfolio.stockAcquired(symbol, amount)).andFinally();
//	}

	Completes<PortfolioState> portfolioCreated(final String owner);

	Completes<PortfolioState> stockAcquired(final String symbol, final int amount);

}
