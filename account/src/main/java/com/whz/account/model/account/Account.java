/*
 * Copyright © 2020, Fachgruppe Informatik WHZ <lationts@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.model.account;

import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

public interface Account {

  static Completes<AccountState> defineWith(final Stage stage, final double balance) {
    final Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
    final Account account =
        stage.actorFor(
            Account.class,
            Definition.has(AccountEntity.class, Definition.parameters(address.idString())),
            address);
    return account.accountCreated(balance);
  }

  Completes<AccountState> accountCreated(final double balance);
}
