/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.exchange;

import com.whz.feedback.model.feedback.FeedbackState;

/**
 * This class is used to send a feedback message with rabbitmq.
 *
 * @since 1.0.0
 */
public class FeedbackDTO {

  /**
   * identifier of the account
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

  public FeedbackDTO(String id, String message) {
    this.id = id;
    this.message = message;
  }

  public static FeedbackDTO from(FeedbackState state) {
    return new FeedbackDTO(state.accountId, state.message);
  }
}
