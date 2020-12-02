package com.whz.commenttone.infrastructure.persistence;

import com.whz.commenttone.infrastructure.CommentToneData;
import com.whz.commenttone.infrastructure.EventTypes;
import com.whz.commenttone.model.commenttone.CommentTonePublishedEvent;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

public class CommentToneProjectionActor extends StateStoreProjectionActor<CommentToneData> {

    private static final CommentToneData Empty = CommentToneData.empty();

    public CommentToneProjectionActor() {
        super(QueryModelStateStoreProvider.instance().store);
    }

    @Override
    protected CommentToneData currentDataFor(final Projectable projectable) {
        return Empty;
    }

    @Override
    protected CommentToneData merge(
            final CommentToneData previousData,
            final int previousVersion,
            final CommentToneData currentData,
            final int currentVersion) {

        for (final Source<?> event : sources()) {
            switch (EventTypes.valueOf(event.typeName())) {
                case CommentTonePublished:
                    final CommentTonePublishedEvent commentTonePublishedEvent = typed(event);
                    return CommentToneData.from(commentTonePublishedEvent.id, commentTonePublishedEvent.message, commentTonePublishedEvent.sentiment);
                default:
                    logger().warn("Event of type " + event.typeName() + " was not matched.");
                    break;
            }
        }

        return previousData;
    }
}
