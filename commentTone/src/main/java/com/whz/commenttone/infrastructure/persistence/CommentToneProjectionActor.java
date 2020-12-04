package com.whz.commenttone.infrastructure.persistence;

import com.whz.commenttone.infrastructure.CommentToneData;
import com.whz.commenttone.infrastructure.EventTypes;
import io.vlingo.actors.Logger;
import io.vlingo.lattice.model.projection.Projectable;
import io.vlingo.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.symbio.Source;

public class CommentToneProjectionActor extends StateStoreProjectionActor<CommentToneData> {

    private static final CommentToneData Empty = CommentToneData.empty();

    Logger logger = Logger.basicLogger();

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

        CommentToneData merged = null;

        for (final Source<?> event : sources()) {
            switch (EventTypes.valueOf(event.typeName())) {
                case CommentTonePublished:
                    final CommentToneData commentTonePublished = typed(event);
                    merged = CommentToneData.from(commentTonePublished.id, commentTonePublished.message, currentData.sentiment);
                    break;
                default:
                    merged = Empty;
                    logger().warn("Event of type " + event.typeName() + " was not matched.");
                    break;
            }
        }

        return merged;
    }
}
