/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <lationts@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.model.account;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public final class AccountEntity extends EventSourced implements Account {
  private AccountState state;

  public AccountEntity(final String id) {
    super(id);
    this.state = AccountState.identifiedBy(id);
  }

  public Completes<AccountState> accountCreated(final double balance) {
    return apply(new AccountCreated(state.id, balance), () -> state);
  }

  // =====================================
  // EventSourced
  // =====================================

  static {
    EventSourced.registerConsumer(
        AccountEntity.class, AccountCreated.class, AccountEntity::applyAccountCreated);
  }

  private void applyAccountCreated(final AccountCreated e) {
    state = state.withCreationValues(e.balance);
  }
}
