package com.whz.commenttone.infrastructure.persistence;

import com.whz.commenttone.infrastructure.CommentToneData;
import com.whz.commenttone.infrastructure.EventTypes;
import io.vlingo.lattice.model.DomainEvent;
import io.vlingo.lattice.model.IdentifiedDomainEvent;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Entry;

import java.util.ArrayList;
import java.util.List;

public class CommentToneProjectionActor extends StateStoreProjectionActor<CommentToneData> {
    private static final CommentToneData Empty = CommentToneData.empty();
    private final List<IdentifiedDomainEvent> events;
    private String dataId;

    public CommentToneProjectionActor() {
        super(QueryModelStateStoreProvider.instance().store);
        this.events = new ArrayList<>(2);
    }

    @Override
    protected CommentToneData currentDataFor(final Projectable projectable) {
        return Empty;
    }

    @Override
    protected String dataIdFor(final Projectable projectable) {
        dataId = events.get(0).identity();
        return dataId;
    }

    @Override
    protected CommentToneData merge(
            final CommentToneData previousData,
            final int previousVersion,
            final CommentToneData currentData,
            final int currentVersion) {

        if (previousVersion == currentVersion) {
            return currentData;
        }

        for (final DomainEvent event : events) {
            switch (EventTypes.valueOf(event.typeName())) {
                case CommentTonePublished:
                    return CommentToneData.from(currentData.id, "Handle CommentToneEvent here");   // TODO: implement actual merge
                default:
                    logger().warn("Event of type " + event.typeName() + " was not matched.");
                    break;
            }
        }

        return previousData;
    }

    @Override
    protected void prepareForMergeWith(final Projectable projectable) {
        events.clear();

        for (Entry<?> entry : projectable.entries()) {
            events.add(entryAdapter().anyTypeFromEntry(entry));
        }
    }
}
