package com.whz.stockquote.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.whz.stockquote.infrastructure.StockQuoteData;

public interface StockQuoteQueries {
  Completes<StockQuoteData> stockQuoteOf(String id);
  Completes<Collection<StockQuoteData>> stockQuotes();
}