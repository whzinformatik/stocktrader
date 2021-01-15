/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.infrastructure.persistence;

import com.whz.account.infrastructure.AccountData;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.State.TextState;
import io.vlingo.symbio.StateAdapter;

/** @since 1.0.0 */
public final class AccountDataStateAdapter implements StateAdapter<AccountData, TextState> {

  @Override
  public int typeVersion() {
    return 1;
  }

  /** @since 1.0.0 */
  @Override
  public AccountData fromRawState(TextState raw) {
    return JsonSerialization.deserialized(raw.data, raw.typed());
  }

  /** @since 1.0.0 */
  @Override
  public <ST> ST fromRawState(TextState raw, Class<ST> stateType) {
    return JsonSerialization.deserialized(raw.data, raw.typed());
  }

  /** @since 1.0.0 */
  @Override
  public TextState toRawState(String id, AccountData state, int stateVersion, Metadata metadata) {
    final String serialization = JsonSerialization.serialized(state);
    return new TextState(
        id, AccountData.class, typeVersion(), serialization, stateVersion, metadata);
  }

  /** @since 1.0.0 */
  @Override
  public TextState toRawState(AccountData state, int stateVersion, Metadata metadata) {
    final String serialization = JsonSerialization.serialized(state);
    return new TextState(
        state.id, AccountData.class, typeVersion(), serialization, stateVersion, metadata);
  }
}
