package com.whz.commenttone.infrastructure.persistence;

import com.whz.commenttone.model.commenttone.CommentTonePublishedEvent;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

public final class CommentToneAdapter implements EntryAdapter<CommentTonePublishedEvent, TextEntry> {

    @Override
    public CommentTonePublishedEvent fromEntry(final TextEntry entry) {
        return JsonSerialization.deserialized(entry.entryData(), entry.typed());
    }

    @Override
    public TextEntry toEntry(final CommentTonePublishedEvent source, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new TextEntry(CommentTonePublishedEvent.class, 1, serialization, metadata);
    }

    @Override
    public TextEntry toEntry(final CommentTonePublishedEvent source, final String id, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new TextEntry(id, CommentTonePublishedEvent.class, 1, serialization, metadata);
    }

    @Override
    public TextEntry toEntry(final CommentTonePublishedEvent source, final int version, final String id, final Metadata metadata) {
        final String serialization = JsonSerialization.serialized(source);
        return new TextEntry(id, CommentTonePublishedEvent.class, 1, serialization, version, metadata);
    }
}
