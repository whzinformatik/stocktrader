package com.whz.feedback.model.feedback;

import com.whz.feedback.resource.Publisher;
import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FeedbackActor extends EventSourced implements Feedback {

  private final Logger logger = LoggerFactory.getLogger(FeedbackActor.class);

  private static final String EXCHANGE_NAME = "feedback";

  private final Publisher<FeedbackState> publisher;

  private FeedbackState state;

  public FeedbackActor(final String id) {
    super(id);
    this.state = FeedbackState.identifiedBy(id);

    final String serviceName =
        Optional.ofNullable(System.getenv("RABBITMQ_SERVICE")).orElse("localhost");
    this.publisher = new Publisher<>(serviceName);
  }

  @Override
  public Completes<FeedbackState> defineWith(final String message) {
    return apply(new FeedbackSubmitted(state.id, message), () -> state);
  }

  // =====================================
  // EventSourced
  // =====================================

  static {
    EventSourced.registerConsumer(
        FeedbackActor.class, FeedbackSubmitted.class, FeedbackActor::applyFeedbackMessage);
  }

  private void applyFeedbackMessage(final FeedbackSubmitted e) {
    state = state.withMessage(e.message);

    try {
      publisher.send(EXCHANGE_NAME, state);
    } catch (Exception ex) {
      logger.error("cannot publish message", ex);
    }
  }
}
