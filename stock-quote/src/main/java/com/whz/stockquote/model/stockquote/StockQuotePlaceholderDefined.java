package com.whz.stockquote.model.stockquote;

import io.vlingo.lattice.model.DomainEvent;

public final class StockQuotePlaceholderDefined extends DomainEvent {
  public final String id;
  public final String placeholderValue;

  public StockQuotePlaceholderDefined(final String id, final String placeholderValue) {
    this.id = id;
    this.placeholderValue = placeholderValue;
  }
}
