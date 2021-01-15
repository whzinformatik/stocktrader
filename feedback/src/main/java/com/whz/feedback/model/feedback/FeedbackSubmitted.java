/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.model.feedback;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

/**
 * This class is used to send all necessary feedback information from the request as an event.
 *
 * @since 1.0.0
 */
public final class FeedbackSubmitted extends IdentifiedDomainEvent {

  /**
   * identifier of the feedback message
   *
   * @since 1.0.0
   */
  public final String id;

  /**
   * identifier of the portfolio
   *
   * @since 1.0.0
   */
  public final String portfolioId;

  /**
   * content of the feedback message
   *
   * @since 1.0.0
   */
  public final String message;

  /**
   * Create an event if a feedback is submitted.
   *
   * @param id id of the feedback message
   * @param message content of the feedback message
   * @param portfolioId identifier of the portfolio
   * @since 1.0.0
   */
  public FeedbackSubmitted(final String id, final String message, final String portfolioId) {
    this.id = id;
    this.message = message;
    this.portfolioId = portfolioId;
  }

  @Override
  public String identity() {
    return id;
  }
}
