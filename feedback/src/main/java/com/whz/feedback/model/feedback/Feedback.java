/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
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

public interface Feedback {

  static Completes<FeedbackState> defineWith(final Stage stage, final String message) {
    final Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
    final Feedback feedbackActor =
        stage.actorFor(
            Feedback.class,
            Definition.has(FeedbackActor.class, Definition.parameters(address.idString())),
            address);
    return feedbackActor.defineWith(message);
  }

  Completes<FeedbackState> defineWith(final String message);
}
