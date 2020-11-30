/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.infrastructure;

import com.whz.feedback.model.feedback.FeedbackState;
import java.util.List;
import java.util.stream.Collectors;

public class FeedbackData {

  public final String id;

  public final String message;

  public static FeedbackData empty() {
    return new FeedbackData("", "");
  }

  public static FeedbackData from(final FeedbackState state) {
    return new FeedbackData(state.id, state.message);
  }

  public static List<FeedbackData> from(final List<FeedbackState> states) {
    return states.stream().map(FeedbackData::from).collect(Collectors.toList());
  }

  public static FeedbackData from(final String id, final String message) {
    return new FeedbackData(id, message);
  }

  public static FeedbackData just(final String message) {
    return new FeedbackData("", message);
  }

  private FeedbackData(final String id, final String message) {
    this.id = id;
    this.message = message;
  }

  @Override
  public String toString() {
    return "FeedbackData [id=" + id + " message=" + message + "]";
  }
}
