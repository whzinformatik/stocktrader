package com.whz.commenttone.model.commenttone;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface CommentTone {

    static Completes<CommentToneState> defineWith(final Stage stage, final String message, String sentiment) {
        final Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
        final CommentTone commentTone = stage.actorFor(CommentTone.class, Definition.has(CommentToneActor.class, Definition.parameters(address.idString())), address);
        return commentTone.defineWith(message, sentiment);
    }

    Completes<CommentToneState> defineWith(final String message, String sentiment);
}
