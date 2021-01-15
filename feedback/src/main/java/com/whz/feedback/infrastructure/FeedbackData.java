/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.infrastructure;

import com.whz.feedback.model.feedback.FeedbackState;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents a DTO for a feedback message.
 *
 * @since 1.0.0
 */
public class FeedbackData {

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

  /** identifier of the portfolio
   * @since 1.0.0
   */
  public final String portfolioId;

  /**
   * Create an empty DTO for a feedback message.
   *
   * @return empty feedback DTO
   * @since 1.0.0
   */
  public static FeedbackData empty() {
    return new FeedbackData("", "", "");
  }

  /**
   * Create a new feedback DTO with the state of another feedback.
   *
   * @param state other feedback state
   * @return feedback DTO with identifiers and message
   * @since 1.0.0
   */
  public static FeedbackData from(final FeedbackState state) {
    return new FeedbackData(state.id, state.message, state.portfolioId);
  }

  /**
   * Create many feedback DTOs from many other feedback states.
   *
   * @param states other feedback states
   * @return feedback DTOs with identifiers and message
   * @since 1.0.0
   */
  public static List<FeedbackData> from(final List<FeedbackState> states) {
    return states.stream().map(FeedbackData::from).collect(Collectors.toList());
  }

  /**
   * Create a new DTO for a feedback message.
   *
   * @param id identifier of the feedback message
   * @param message content of the feedback message
   * @param portfolioId identifier of the portfolio
   * @return feedback DTO with identifier and message
   * @since 1.0.0
   */
  public static FeedbackData from(final String id, final String message, final String portfolioId) {
    return new FeedbackData(id, message, portfolioId);
  }

  /**
   * Create a new DTO for a feedback message.
   *
   * @param message content of the feedback message
   *                @param portfolioId identifier of the portfolio
   * @return feedback DTO with message and without an identifier
   * @since 1.0.0
   */
  public static FeedbackData just(final String message, final String portfolioId) {
    return new FeedbackData("", message, portfolioId);
  }

  /**
   * Create a new feedback DTO.
   *
   * @param id identifier of the feedback message
   * @param message content of the feedback message
   * @param portfolioId identifier of the portfolio
   * @since 1.0.0
   */
  private FeedbackData(final String id, final String message, final String portfolioId) {
    this.id = id;
    this.message = message;
    this.portfolioId = portfolioId;
  }

  @Override
  public String toString() {
    return String.format("FeedbackData [id=%s message=%s portfolio=%s]", id, message, portfolioId);
  }
}
