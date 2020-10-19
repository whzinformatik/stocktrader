package com.whz.stock-quote.model.stockquote;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface StockQuote {

  static Completes<StockQuoteState> definePlaceholder(final Stage stage, final String placeholderValue) {
    final Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
    final StockQuote stockQuote = stage.actorFor(StockQuote.class, Definition.has(StockQuoteEntity.class, Definition.parameters(address.idString())), address);
    return stockQuote.definePlaceholder(placeholderValue);
  }

  Completes<StockQuoteState> definePlaceholder(final String placeholderValue);

}