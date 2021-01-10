/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.model.feedback;

import com.whz.feedback.resource.Publisher;
import com.whz.feedback.utils.EnvUtils;
import io.vlingo.actors.Logger;
import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * This class is used to handle all actions for a feedback message.
 *
 * @since 1.0.0
 */
public final class FeedbackActor extends EventSourced implements Feedback {

  /**
   * name of the feedback exchange
   *
   * @since 1.0.0
   */
  public static final String EXCHANGE_NAME = "feedback";

  private final Logger logger = Logger.basicLogger();

  private final Publisher<FeedbackState> publisher;

  private FeedbackState state;

  /**
   * Create a new actor to handle all feedback actions.
   *
   * @param id identifier of the feedback message
   * @since 1.0.0
   */
  public FeedbackActor(final String id) throws IOException, TimeoutException {
    super(id);
    this.state = FeedbackState.identifiedBy(id);
    this.publisher = new Publisher<>(EnvUtils.RABBITMQ_SERVICE.get());
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

  /**
   * Handle the {@link FeedbackSubmitted} event to change the state and send the feedback to
   * rabbitmq.
   *
   * @param e created feedback submitted event
   * @since 1.0.0
   */
  private void applyFeedbackMessage(final FeedbackSubmitted e) {
    state = state.withMessage(e.message);

    try {
      publisher.send(EXCHANGE_NAME, state);
    } catch (Exception ex) {
      logger.error("cannot publish message", ex);
    }
  }
}
