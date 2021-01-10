/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.model.feedback;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

/**
 * This interface is used to declare all methods for a feedback message.
 *
 * @since 1.0.0
 */
public interface Feedback {

  /**
   * Create a new {@link FeedbackActor} with an identifier and a message.
   *
   * @param stage stage of the current world
   * @param message content of the feedback message
   * @return response for an asynchronous call with a potential result
   * @since 1.0.0
   */
  static Completes<FeedbackState> defineWith(final Stage stage, final String message) {
    final Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
    final Feedback feedbackActor =
        stage.actorFor(
            Feedback.class,
            Definition.has(FeedbackActor.class, Definition.parameters(address.idString())),
            address);
    return feedbackActor.defineWith(message);
  }

  /**
   * Add content for the current state of the feedback message.
   *
   * @param message content of the feedback message
   * @return response for an asynchronous call with potential result
   * @since 1.0.0
   */
  Completes<FeedbackState> defineWith(final String message);
}
