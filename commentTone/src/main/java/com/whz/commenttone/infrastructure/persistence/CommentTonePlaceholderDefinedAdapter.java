package com.whz.commenttone.infrastructure.persistence;

import com.whz.commenttone.model.commenttone.CommentTonePlaceholderDefined;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public final class CommentTonePlaceholderDefinedAdapter implements EntryAdapter<CommentTonePlaceholderDefined, TextEntry> {

    @Override
    public CommentTonePlaceholderDefined fromEntry(final TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData(), entry.typed());
    }

    @Override
    public TextEntry toEntry(final CommentTonePlaceholderDefined source, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new TextEntry(CommentTonePlaceholderDefined.class, 1, serialization, metadata);
    }

    @Override
    public TextEntry toEntry(final CommentTonePlaceholderDefined source, final String id, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new TextEntry(id, CommentTonePlaceholderDefined.class, 1, serialization, metadata);
    }

    @Override
    public TextEntry toEntry(final CommentTonePlaceholderDefined source, final int version, final String id, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new TextEntry(id, CommentTonePlaceholderDefined.class, 1, serialization, version, metadata);
    }
}
