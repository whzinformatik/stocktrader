package com.whz.stockquote.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Collection;

import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

import com.whz.stockquote.infrastructure.StockQuoteData;

public class StockQuoteQueriesActor extends StateStoreQueryActor implements StockQuoteQueries {

  public StockQuoteQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<StockQuoteData> stockQuoteOf(String id) {
    return queryStateFor(id, StockQuoteData.class, StockQuoteData.empty());
  }

  @Override
  public Completes<Collection<StockQuoteData>> stockQuotes() {
    return streamAllOf(StockQuoteData.class, new ArrayList<>());
  }

}
