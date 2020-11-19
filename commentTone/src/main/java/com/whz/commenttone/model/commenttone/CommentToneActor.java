package com.whz.commenttone.model.commenttone;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public final class CommentToneActor extends EventSourced implements CommentTone {
    static {
        EventSourced.registerConsumer(CommentToneActor.class, CommentTonePublishedEvent.class, CommentToneActor::applyCommentToneMessage);
    }

    private CommentToneState state;

    public CommentToneActor(final String id) {
        super(id);
        this.state = CommentToneState.identifiedBy(id);
    }

    //=====================================
    // EventSourced
    //=====================================

    public Completes<CommentToneState> defineWith(final String message) {
        return apply(new CommentTonePublishedEvent(state.id, message), () -> state);
    }

    private void applyCommentToneMessage(final CommentTonePublishedEvent e) {
        state = state.withMessage(e.message);
    }
}
