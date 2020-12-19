package com.whz.portfolio.infrastructure.persistence;

import com.whz.portfolio.infrastructure.EventTypes;
import com.whz.portfolio.infrastructure.PortfolioData;
import com.whz.portfolio.model.portfolio.PortfolioCreated;
import com.whz.portfolio.model.portfolio.Stock;
import com.whz.portfolio.model.portfolio.StockAcquired;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

public class PortfolioProjectionActor extends StateStoreProjectionActor<PortfolioData> {
  private static final PortfolioData Empty = PortfolioData.empty();

  public PortfolioProjectionActor() {
    super(QueryModelStateStoreProvider.instance().store);
  }

  @Override
  protected PortfolioData currentDataFor(final Projectable projectable) {
    return Empty;
  }

  protected PortfolioData merge(
      final PortfolioData previousData,
      final int previousVersion,
      final PortfolioData currentData,
      final int currentVersion) {

    if (previousVersion == currentVersion) {
      System.out.println(previousVersion);
      return previousData;
    }

    for (final Source<?> event : sources()) {
      switch (EventTypes.valueOf(event.typeName())) {
        case PortfolioCreated:
          final PortfolioCreated portfolioCreated = typed(event);
          currentData.id = portfolioCreated.id;
          currentData.owner = portfolioCreated.owner;
          break;
        case StockAcquired:
          final StockAcquired stockAcquired = typed(event);
          final Stock stock =
              new Stock(
                  stockAcquired.symbol,
                  stockAcquired.acquisitionMarketTime,
                  stockAcquired.amount,
                  stockAcquired.acquisitionMarketPrice);
          currentData.id = previousData.id;
          currentData.owner = previousData.owner;
          currentData.stocks = previousData.stocks;
          currentData.stocks.add(stock);
          break;
        default:
          logger().warn("Event of type " + event.typeName() + " was not matched.");
          break;
      }
    }
    return currentData;
  }
}
