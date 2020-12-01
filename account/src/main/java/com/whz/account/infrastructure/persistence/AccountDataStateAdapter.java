package com.whz.account.infrastructure.persistence;

import com.whz.account.infrastructure.AccountData;
import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.State.TextState;
import io.vlingo.symbio.StateAdapter;

public final class AccountDataStateAdapter implements StateAdapter<AccountData, TextState> {

  @Override
  public int typeVersion() {
    return 1;
  }

  @Override
  public AccountData fromRawState(TextState raw) {
    return JsonSerialization.deserialized(raw.data, raw.typed());
  }

  @Override
  public <ST> ST fromRawState(TextState raw, Class<ST> stateType) {
    return JsonSerialization.deserialized(raw.data, raw.typed());
  }

  @Override
  public TextState toRawState(String id, AccountData state, int stateVersion, Metadata metadata) {
    final String serialization = JsonSerialization.serialized(state);
    return new TextState(
        id, AccountData.class, typeVersion(), serialization, stateVersion, metadata);
  }

  @Override
  public TextState toRawState(AccountData state, int stateVersion, Metadata metadata) {
    final String serialization = JsonSerialization.serialized(state);
    return new TextState(
        state.id, AccountData.class, typeVersion(), serialization, stateVersion, metadata);
  }
}
