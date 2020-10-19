package com.whz.account.model.account;

import java.util.Collections;
import java.util.List;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public final class AccountEntity extends EventSourced implements Account {
  private AccountState state;

  public AccountEntity(final String id) {
    super(id);
    this.state = AccountState.identifiedBy(id);
  }

  public Completes<AccountState> definePlaceholder(final String value) {
    return apply(new AccountPlaceholderDefined(state.id, value), () -> state);
  }

  //=====================================
  // EventSourced
  //=====================================

  static {
    EventSourced.registerConsumer(AccountEntity.class, AccountPlaceholderDefined.class, AccountEntity::applyAccountPlaceholderDefined);
  }

  private void applyAccountPlaceholderDefined(final AccountPlaceholderDefined e) {
    state = state.withPlaceholderValue(e.placeholderValue);
  }
}
