package com.whz.portfolio.infrastructure.persistence;

import com.whz.portfolio.infrastructure.EventTypes;
import com.whz.portfolio.infrastructure.PortfolioData;
import com.whz.portfolio.model.portfolio.PortfolioCreated;
import com.whz.portfolio.model.portfolio.Stock;
import com.whz.portfolio.model.portfolio.StockAcquired;

import io.vlingo.lattice.model.DomainEvent;
import io.vlingo.lattice.model.IdentifiedDomainEvent;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.Source;
import java.util.ArrayList;
import java.util.List;

public class PortfolioProjectionActor extends StateStoreProjectionActor<PortfolioData> {
	private static final PortfolioData Empty = PortfolioData.empty();

	public PortfolioProjectionActor() {
		super(QueryModelStateStoreProvider.instance().store);
	}

	@Override
	protected PortfolioData currentDataFor(final Projectable projectable) {
		return Empty;
	}

	protected PortfolioData merge(final PortfolioData previousData, final int previousVersion,
			final PortfolioData currentData, final int currentVersion) {
		if(previousVersion == currentVersion) {
			System.out.println(previousVersion);
		}
		for (final Source<?> event : sources()) {
			switch (EventTypes.valueOf(event.typeName())) {
			case PortfolioCreated:
				final PortfolioCreated portfolioCreated = typed(event);
				//merged = PortfolioData.from(portfolioCreated.id, portfolioCreated.owner);
				currentData.id = portfolioCreated.id;
				currentData.owner = portfolioCreated.owner;
				break;
			case StockAcquired:
				final StockAcquired stockAcquired = typed(event);
				final Stock stock = new Stock(stockAcquired.symbol, "", 0L, stockAcquired.amount, 0.0, 0.0, "");
				
				currentData.id = previousData.id;
				currentData.owner = previousData.owner;
				currentData.stockList = previousData.stockList;
				currentData.stockList.add(stock);
				
//				merged = PortfolioData.from(previousData.id, previousData.owner);
//				merged.stockList.addAll(previousData.stockList);
//				merged.stockList.add(stock);
				break;
			default:
//				merged = Empty;
				logger().warn("Event of type " + event.typeName() + " was not matched.");
				break;
			}
		}
		return currentData;
	}

}
