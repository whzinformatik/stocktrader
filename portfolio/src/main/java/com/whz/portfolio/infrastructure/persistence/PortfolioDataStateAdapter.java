/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.portfolio.infrastructure.persistence;

import com.whz.portfolio.infrastructure.PortfolioData;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.State.TextState;
import io.vlingo.symbio.StateAdapter;

public class PortfolioDataStateAdapter implements StateAdapter<PortfolioData, TextState> {

  @Override
  public int typeVersion() {
    return 1;
  }

  @Override
  public PortfolioData fromRawState(final TextState raw) {
    return JsonSerialization.deserialized(raw.data, raw.typed());
  }

  @Override
  public TextState toRawState(String id, PortfolioData state, int stateVersion, Metadata metadata) {
    final String serialization = JsonSerialization.serialized(state);
    return new TextState(
        id, PortfolioData.class, typeVersion(), serialization, stateVersion, metadata);
  }

  @Override
  public TextState toRawState(PortfolioData state, int stateVersion, Metadata metadata) {
    final String serialization = JsonSerialization.serialized(state);
    return new TextState(
        state.id, PortfolioData.class, typeVersion(), serialization, stateVersion, metadata);
  }

  @Override
  public <ST> ST fromRawState(TextState raw, Class<ST> stateType) {
    return JsonSerialization.deserialized(raw.data, stateType);
  }
}
