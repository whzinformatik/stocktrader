/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.model.feedback;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public final class FeedbackSubmitted extends IdentifiedDomainEvent {

  public final String id;
  public final String message;

  public FeedbackSubmitted(final String id, final String message) {
    this.id = id;
    this.message = message;
  }

  @Override
  public String identity() {
    return id;
  }
}
