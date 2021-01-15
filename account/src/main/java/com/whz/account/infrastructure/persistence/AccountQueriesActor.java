/*
 * Copyright © 2020-2021, Fachgruppe Informatik WHZ <help.flaxel@gmail.com>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package com.whz.account.infrastructure.persistence;

import com.whz.account.infrastructure.AccountData;
import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;
import java.util.ArrayList;
import java.util.Collection;

/** @since 1.0.0 */
public class AccountQueriesActor extends StateStoreQueryActor implements AccountQueries {

  public AccountQueriesActor(StateStore store) {
    super(store);
  }

  /** @since 1.0.0 */
  @Override
  public Completes<AccountData> accountOf(String id) {
    return queryStateFor(id, AccountData.class, AccountData.empty());
  }

  /** @since 1.0.0 */
  @Override
  public Completes<Collection<AccountData>> accounts() {
    return streamAllOf(AccountData.class, new ArrayList<>());
  }
}
