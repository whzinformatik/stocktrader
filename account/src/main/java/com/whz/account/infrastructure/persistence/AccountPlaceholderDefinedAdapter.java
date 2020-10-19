package com.whz.account.infrastructure.persistence;

import com.whz.account.model.account.AccountPlaceholderDefined;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public final class AccountPlaceholderDefinedAdapter implements EntryAdapter<AccountPlaceholderDefined,TextEntry> {
  
  @Override
  public AccountPlaceholderDefined fromEntry(final TextEntry entry) {
    return JsonSerialization.deserialized(entry.entryData(), entry.typed());
  }

  @Override
  public TextEntry toEntry(final AccountPlaceholderDefined source, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(AccountPlaceholderDefined.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final AccountPlaceholderDefined source, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, AccountPlaceholderDefined.class, 1, serialization, metadata);
  }

  @Override
  public TextEntry toEntry(final AccountPlaceholderDefined source, final int version, final String id, final Metadata metadata) {
    final String serialization = JsonSerialization.serialized(source);
    return new TextEntry(id, AccountPlaceholderDefined.class, 1, serialization, version, metadata);
  }
}
