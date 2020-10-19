package com.whz.stockquote.model.stockquote;

import java.util.Collections;
import java.util.List;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public final class StockQuoteEntity extends EventSourced implements StockQuote {
  private StockQuoteState state;

  public StockQuoteEntity(final String id) {
    super(id);
    this.state = StockQuoteState.identifiedBy(id);
  }

  public Completes<StockQuoteState> definePlaceholder(final String value) {
    return apply(new StockQuotePlaceholderDefined(state.id, value), () -> state);
  }

  //=====================================
  // EventSourced
  //=====================================

  static {
    EventSourced.registerConsumer(StockQuoteEntity.class, StockQuotePlaceholderDefined.class, StockQuoteEntity::applyStockQuotePlaceholderDefined);
  }

  private void applyStockQuotePlaceholderDefined(final StockQuotePlaceholderDefined e) {
    state = state.withPlaceholderValue(e.placeholderValue);
  }
}
