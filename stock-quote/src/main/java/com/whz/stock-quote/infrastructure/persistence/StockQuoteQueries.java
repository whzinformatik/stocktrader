package com.whz.stock-quote.infrastructure.persistence;

import java.util.Collection;
import io.vlingo.common.Completes;

import com.whz.stock-quote.infrastructure.StockQuoteData;

public interface StockQuoteQueries {
  Completes<StockQuoteData> stockQuoteOf(String id);
  Completes<Collection<StockQuoteData>> stockQuotes();
}