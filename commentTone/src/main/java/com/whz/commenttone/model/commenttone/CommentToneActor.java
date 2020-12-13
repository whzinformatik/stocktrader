package com.whz.commenttone.model.commenttone;

import com.whz.commenttone.resource.rabbitmq.CommentToneConsumer;
import com.whz.commenttone.resource.rabbitmq.CommentTonePublisher;
import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

import java.util.Optional;

public final class CommentToneActor extends EventSourced implements CommentTone {

    private final CommentTonePublisher<CommentToneState> publisher;
    private static final String SEND_EXCHANGE_NAME = "commentTone";

    private final CommentToneConsumer consumer;
    private static final String CONSUME_EXCHANGE_NAME = "feedback";

    private CommentToneState state;

    public CommentToneActor(final String id) {
        super(id);
        this.state = CommentToneState.identifiedBy(id);

        final String serviceName = Optional.ofNullable(
                System.getenv("RABBITMQ_SERVICE"))
                .orElse("localhost");

        this.publisher = new CommentTonePublisher<>(serviceName);
        this.consumer = new CommentToneConsumer(serviceName);
    }

    @Override
    public Completes<CommentToneState> defineWith(final String message, final String sentiment) {
        return apply(new CommentTonePublishedEvent(state.id, message, sentiment), () -> state);
    }

    //=====================================
    // EventSourced
    //=====================================

    static {
        EventSourced.registerConsumer(
                CommentToneActor.class,
                CommentTonePublishedEvent.class,
                CommentToneActor::applyCommentToneMessage);
    }

    private void applyCommentToneMessage(final CommentTonePublishedEvent e) {
        state = state.withMessage(e.message);

//            consumer.consume(CONSUME_EXCHANGE_NAME);
//            publisher.send(SEND_EXCHANGE_NAME, state);

    }
}
