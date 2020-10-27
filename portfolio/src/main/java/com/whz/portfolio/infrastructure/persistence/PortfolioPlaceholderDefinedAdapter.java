package com.whz.portfolio.infrastructure.persistence;

import com.whz.portfolio.model.portfolio.PortfolioCreated;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public final class PortfolioPlaceholderDefinedAdapter implements EntryAdapter<PortfolioCreated,TextEntry> {
  
  @Override
  public PortfolioCreated fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final PortfolioCreated source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(PortfolioCreated.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final PortfolioCreated source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, PortfolioCreated.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final PortfolioCreated source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, PortfolioCreated.class, 1, serialization, version, metadata);
  }
}
