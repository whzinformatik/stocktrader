/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.feedback.infrastructure.persistence;

import com.whz.feedback.infrastructure.FeedbackData;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.State.TextState;
import io.vlingo.symbio.StateAdapter;

/**
 * Adapts the {@link FeedbackData} to the {@link TextState}, and the {@link TextState} to the {@link FeedbackData}.
 * @since 1.0.0
 */
public class FeedbackDataStateAdapter implements StateAdapter<FeedbackData, TextState> {

  @Override
  public int typeVersion() {
    return 1;
  }

  @Override
  public FeedbackData fromRawState(final TextState raw) {
    return JsonSerialization.deserialized(raw.data, raw.typed());
  }

  @Override
  public TextState toRawState(String id, FeedbackData state, int stateVersion, Metadata metadata) {
    final String serialization = JsonSerialization.serialized(state);
    return new TextState(
        id, FeedbackData.class, typeVersion(), serialization, stateVersion, metadata);
  }

  @Override
  public TextState toRawState(FeedbackData state, int stateVersion, Metadata metadata) {
    final String serialization = JsonSerialization.serialized(state);
    return new TextState(
        state.id, FeedbackData.class, typeVersion(), serialization, stateVersion, metadata);
  }

  @Override
  public <ST> ST fromRawState(TextState raw, Class<ST> stateType) {
    return JsonSerialization.deserialized(raw.data, stateType);
  }
}
