/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.model.feedback;

/**
 * This class is used to save the current state of a feedback message.
 *
 * @since 1.0.0
 */
public final class FeedbackState {

  /**
   * identifier of the feedback message
   *
   * @since 1.0.0
   */
  public final String id;

  /**
   * content of the feedback message
   *
   * @since 1.0.0
   */
  public final String message;

  /**
   * identifier of the portfolio
   *
   * @since 1.0.0
   */
  public final String portfolioId;

  /**
   * Create a feedback state with an identifier of the feedback message.
   *
   * @param id identifier of the feedback message
   * @return new feedback state
   * @since 1.0.0
   */
  public static FeedbackState identifiedBy(final String id) {
    return new FeedbackState(id);
  }

  /**
   * Check if the feedback state does exist.
   *
   * @return true if the identifier is not set, otherwise false
   * @since 1.0.0
   */
  public boolean doesNotExist() {
    return id == null;
  }

  /**
   * Check if the feedback state can only be identified.
   *
   * @return true if the identifier is set and all other variables are not set, otherwise false
   * @since 1.0.0
   */
  public boolean isIdentifiedOnly() {
    return id != null && message == null && portfolioId == null;
  }

  /**
   * Add content to the feedback message.
   *
   * @param message feedback content of the user
   * @return updated feedback state
   * @since 1.0.0
   */
  public FeedbackState withMessage(final String message) {
    return new FeedbackState(this.id, message, this.portfolioId);
  }

  /**
   * Add portfolioId to the feedback message.
   *
   * @param portfolioId identifier of the portfolio
   * @return updated feedback state
   * @since 1.0.0
   */
  public FeedbackState withPortfolioId(final String portfolioId) {
    return new FeedbackState(this.id, this.message, portfolioId);
  }

  /**
   * Create a feedback state with identifier and message.
   *
   * @param id identifier of the feedback message
   * @param message content of the feedback message
   * @param portfolioId identifier of the portfolio
   * @since 1.0.0
   */
  private FeedbackState(final String id, final String message, final String portfolioId) {
    this.id = id;
    this.message = message;
    this.portfolioId = portfolioId;
  }

  /**
   * Create a feedback state with identifier.
   *
   * @param id identifier of the feedback message
   * @since 1.0.0
   */
  private FeedbackState(final String id) {
    this(id, null, null);
  }

  /**
   * Create a feedback state.
   *
   * @since 1.0.0
   */
  private FeedbackState() {
    this(null, null, null);
  }
}
