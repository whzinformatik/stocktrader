package com.whz.account.infrastructure.persistence;

import com.whz.account.infrastructure.AccountData;
import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;
import java.util.ArrayList;
import java.util.Collection;

public class AccountQueriesActor extends StateStoreQueryActor implements AccountQueries {

  public AccountQueriesActor(StateStore store) {
    super(store);
  }

  @Override
  public Completes<AccountData> accountOf(String id) {
    return queryStateFor(id, AccountData.class, AccountData.empty());
  }

  @Override
  public Completes<Collection<AccountData>> accounts() {
    return streamAllOf(AccountData.class, new ArrayList<>());
  }
}
