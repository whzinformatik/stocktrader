package com.whz.portfolio.infrastructure.persistence;

import com.whz.portfolio.infrastructure.PortfolioData;
import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;
import java.util.ArrayList;
import java.util.Collection;

public class PortfolioQueriesActor extends StateStoreQueryActor implements PortfolioQueries {

  public PortfolioQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<PortfolioData> portfolioOf(String id) {
    return queryStateFor(id, PortfolioData.class, PortfolioData.empty());
  }

  @Override
  public Completes<Collection<PortfolioData>> portfolios() {
    return streamAllOf(PortfolioData.class, new ArrayList<>());
  }
}
