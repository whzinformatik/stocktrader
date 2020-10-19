package com.whz.stock-quote.model.stockquote;

public final class StockQuoteState {
  public final String id;
  public final String placeholderValue;

  public static StockQuoteState identifiedBy(final String id) {
    return new StockQuoteState(id);
  }

  public boolean doesNotExist() {
    return id == null;
  }

  public boolean isIdentifiedOnly() {
    return id != null && placeholderValue == null;
  }

  public StockQuoteState withPlaceholderValue(final String value) {
    return new StockQuoteState(this.id, value);
  }

  private StockQuoteState(final String id, final String value) {
    this.id = id;
    this.placeholderValue = value;
  }

  private StockQuoteState(final String id) {
    this(id, null);
  }

  private StockQuoteState() {
    this(null, null);
  }
}
