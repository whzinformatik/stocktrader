package com.whz.commenttone.model.commenttone;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface CommentTone {

    static Completes<CommentToneState> definePlaceholder(final Stage stage, final String placeholderValue) {
        final Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
        final CommentTone commentTone = stage.actorFor(CommentTone.class, Definition.has(CommentToneEntity.class, Definition.parameters(address.idString())), address);
        return commentTone.definePlaceholder(placeholderValue);
    }

    Completes<CommentToneState> definePlaceholder(final String placeholderValue);

}