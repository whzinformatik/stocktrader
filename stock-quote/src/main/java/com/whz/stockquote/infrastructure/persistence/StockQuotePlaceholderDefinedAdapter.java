package com.whz.stockquote.infrastructure.persistence;

import com.whz.stockquote.model.stockquote.StockQuotePlaceholderDefined;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public final class StockQuotePlaceholderDefinedAdapter implements EntryAdapter<StockQuotePlaceholderDefined,TextEntry> {
  
  @Override
  public StockQuotePlaceholderDefined fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final StockQuotePlaceholderDefined source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(StockQuotePlaceholderDefined.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final StockQuotePlaceholderDefined source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, StockQuotePlaceholderDefined.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final StockQuotePlaceholderDefined source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, StockQuotePlaceholderDefined.class, 1, serialization, version, metadata);
  }
}
