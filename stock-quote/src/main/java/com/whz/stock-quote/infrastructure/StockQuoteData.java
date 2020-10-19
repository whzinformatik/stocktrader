package com.whz.stock-quote.infrastructure;

import java.util.ArrayList;
import java.util.List;
import com.whz.stock-quote.model.stockquote.StockQuoteState;

public class StockQuoteData {
  public final String id;
  public final String placeholderValue;

  public static StockQuoteData empty() {
    return new StockQuoteData("", "");
  }

  public static StockQuoteData from(final StockQuoteState state) {
    return new StockQuoteData(state.id, state.placeholderValue);
  }

  public static List<StockQuoteData> from(final List<StockQuoteState> states) {
    final List<StockQuoteData> data = new ArrayList<>(states.size());

    for (final StockQuoteState state : states) {
      data.add(StockQuoteData.from(state));
    }

    return data;
  }

  public static StockQuoteData from(final String id, final String placeholderValue) {
    return new StockQuoteData(id, placeholderValue);
  }

  public static StockQuoteData just(final String placeholderValue) {
    return new StockQuoteData("", placeholderValue);
  }

  @Override
  public String toString() {
    return "StockQuoteData [id=" + id + " placeholderValue=" + placeholderValue + "]";
  }

  private StockQuoteData(final String id, final String placeholderValue) {
    this.id = id;
    this.placeholderValue = placeholderValue;
  }
}
