/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.model.feedback;

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

  private final Logger logger = Logger.basicLogger();

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
  }

  @Override
  public Completes<FeedbackState> defineWith(final String message, final String accountId) {
    return apply(new FeedbackSubmitted(state.id, message, accountId), () -> state);
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
    state = state.withMessage(e.message).withPortfolioId(e.accountId);
  }
}
