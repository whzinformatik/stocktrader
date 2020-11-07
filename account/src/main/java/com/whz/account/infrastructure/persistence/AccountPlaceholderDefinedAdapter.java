package com.whz.account.infrastructure.persistence;

import com.whz.account.model.account.AccountCreated;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public final class AccountPlaceholderDefinedAdapter implements EntryAdapter<AccountCreated, TextEntry> {

	@Override
	public AccountCreated fromEntry(final TextEntry entry) {
		return JsonSerialization.deserialized(entry.entryData(), entry.typed());
	}

	@Override
	public TextEntry toEntry(final AccountCreated source, final Metadata metadata) {
		final String serialization = JsonSerialization.serialized(source);
		return new TextEntry(AccountCreated.class, 1, serialization, metadata);
	}

	@Override
	public TextEntry toEntry(final AccountCreated source, final String id, final Metadata metadata) {
		final String serialization = JsonSerialization.serialized(source);
		return new TextEntry(id, AccountCreated.class, 1, serialization, metadata);
	}

	@Override
	public TextEntry toEntry(final AccountCreated source, final int version, final String id, final Metadata metadata) {
		final String serialization = JsonSerialization.serialized(source);
		return new TextEntry(id, AccountCreated.class, 1, serialization, version, metadata);
	}
}
