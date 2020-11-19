package com.whz.commenttone.model.commenttone;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public final class CommentToneEntity extends EventSourced implements CommentTone {
    static {
        EventSourced.registerConsumer(CommentToneEntity.class, CommentTonePlaceholderDefined.class, CommentToneEntity::applyCommentTonePlaceholderDefined);
    }

    private CommentToneState state;

    public CommentToneEntity(final String id) {
        super(id);
        this.state = CommentToneState.identifiedBy(id);
    }

    //=====================================
    // EventSourced
    //=====================================

    public Completes<CommentToneState> definePlaceholder(final String value) {
        return apply(new CommentTonePlaceholderDefined(state.id, value), () -> state);
    }

    private void applyCommentTonePlaceholderDefined(final CommentTonePlaceholderDefined e) {
        state = state.withPlaceholderValue(e.placeholderValue);
    }
}
