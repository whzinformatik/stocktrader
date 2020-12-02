package com.whz.commenttone.infrastructure.persistence;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.Source;

public final class EventAdapter<T extends Source<?>> implements EntryAdapter<T, TextEntry> {

    @Override
    public T fromEntry(final TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData(), entry.typed());
    }

    @Override
    public TextEntry toEntry(final T source, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new TextEntry(source.getClass(), 1, serialization, metadata);
    }

    @Override
    public TextEntry toEntry(final T source, final String id, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new TextEntry(id, source.getClass(), 1, serialization, metadata);
    }

    @Override
    public TextEntry toEntry(
            final T source, final int version, final String id, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new TextEntry(id, source.getClass(), 1, serialization, version, metadata);
    }
}
