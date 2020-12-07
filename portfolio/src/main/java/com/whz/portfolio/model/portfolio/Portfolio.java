package com.whz.portfolio.model.portfolio;

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

	Completes<PortfolioState> portfolioCreated(final String owner);

	Completes<PortfolioState> stockAcquired(String symbol, long acquisitionMarketTime, int amount, double acquisitionMarketPrice);

}
